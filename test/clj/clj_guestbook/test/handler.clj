(ns clj-guestbook.test.handler
  (:require
    [clojure.test :refer :all]
    [ring.mock.request :refer :all]
    [clj-guestbook.handler :refer :all]
    [clj-guestbook.middleware.formats :as formats]
    [muuntaja.core :as m]
    [mount.core :as mount]))

(defn parse-json [body]
  (m/decode formats/instance "application/json" body))

(use-fixtures
  :once
  (fn [f]
    (mount/start #'clj-guestbook.config/env
                 #'clj-guestbook.handler/app-routes)
    (f)))

(deftest test-app
  (testing "main route"
    (let [response ((app) (request :get "/"))]
      (is (= 200 (:status response)))))

  (testing "not-found route"
    (let [response ((app) (request :get "/invalid"))]
      (is (= 404 (:status response))))))
