(ns clj-guestbook.routes.home
  (:require
   [clj-guestbook.layout :as layout]
   [clj-guestbook.db.core :as db]
   [clojure.java.io :as io]
   [clj-guestbook.middleware :as middleware]
   [ring.util.response]
   [ring.util.http-response :as response]))

(defn home-page [request]
;   (layout/render request "home.html" {:docs (-> "docs/docs.md" io/resource slurp)}))
  (layout/render request "home.html" {:messages (db/get-messages)}))

(defn about-page [request]
  (layout/render request "about.html"))

(defn home-routes []
  [""
   {:middleware [middleware/wrap-csrf
                 middleware/wrap-formats]}
   ["/" {:get home-page}]
   ["/about" {:get about-page}]])

