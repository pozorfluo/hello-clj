(ns clj-guestbook.test.db.core
  (:require
   [clj-guestbook.db.core :refer [*db*] :as db]
   [java-time.pre-java8]
   [luminus-migrations.core :as migrations]
   [clojure.test :refer :all]
   [clojure.java.jdbc :as jdbc]
   [clj-guestbook.config :refer [env]]
   [mount.core :as mount]))

(use-fixtures
  :once
  (fn [f]
    (mount/start
     #'clj-guestbook.config/env
     #'clj-guestbook.db.core/*db*)
    (migrations/migrate ["migrate"] (select-keys env [:database-url]))
    (f)))

(deftest test-users
  (jdbc/with-db-transaction [t-conn *db*]
    (jdbc/db-set-rollback-only! t-conn)
    (is (= 1 (db/create-user!
              t-conn
              {:id         "1"
               :first_name "Sam"
               :last_name  "Smith"
               :email      "sam.smith@example.com"
               :pass       "pass"})))
    (is (= {:id         "1"
            :first_name "Sam"
            :last_name  "Smith"
            :email      "sam.smith@example.com"
            :pass       "pass"
            :admin      nil
            :last_login nil
            :is_active  nil}
           (db/get-user t-conn {:id "1"})))))

(deftest test-messages
  (jdbc/with-db-transaction [t-conn *db*]
    (jdbc/db-set-rollback-only! t-conn)
    (is (= 1 (db/save-message!
              t-conn
              {:name "Vedette"
               :message "Ca réseaute ?"}
              {:connection t-conn})))
    (is (= {:name "Vedette" :message "Ca réseaute ?"}
           (-> (db/get-messages t-conn {})
               (first ,,,)
               (select-keys ,,, [:name :message]))))))