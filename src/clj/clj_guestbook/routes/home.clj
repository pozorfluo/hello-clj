(ns clj-guestbook.routes.home
  (:require
   [clj-guestbook.layout :as layout]
   [clj-guestbook.db.core :as db]
   [clojure.java.io :as io]
   [clj-guestbook.middleware :as middleware]
   [ring.util.response]
   [ring.util.http-response :as response]
   [struct.core :as st]))

(defn home-page [{:keys [flash] :as request}]
;   (layout/render request "home.html" {:docs (-> "docs/docs.md" io/resource slurp)}))
  (layout/render request "home.html" (merge {:messages (db/get-messages)} 
        (select-keys flash [:name :message :errors]))))

(defn about-page [request]
    (layout/render request "about.html"))

(def message-schema
  [[:name
    st/required 
    st/string]
   [:message
    st/required
    st/string
    {:message "Your message must be at least 10 characters long !"
     :validate (fn [msg] (>= (count msg) 10))}]])

(defn validate-message [params]
  (first (st/validate params message-schema)))

(defn save-message! [{:keys [params]}]
    (if-let [errors (validate-message params)]
        (-> (response/found "/")
        (assoc ,,, :flash (assoc params :errors errors)))
    (do
        (db/save-message! params)
        (response/found "/"))))

(defn home-routes []
  [""
   {:middleware [middleware/wrap-csrf
                 middleware/wrap-formats]}
   ["/" {:get home-page}]
   ["/about" {:get about-page}]
   ["/message" {:post save-message!}]])

