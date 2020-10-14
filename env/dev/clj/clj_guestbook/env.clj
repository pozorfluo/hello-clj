(ns clj-guestbook.env
  (:require
    [selmer.parser :as parser]
    [clojure.tools.logging :as log]
    [clj-guestbook.dev-middleware :refer [wrap-dev]]))

(def defaults
  {:init
   (fn []
     (parser/cache-off!)
     (log/info "\n-=[clj-guestbook started successfully using the development profile]=-"))
   :stop
   (fn []
     (log/info "\n-=[clj-guestbook has shut down successfully]=-"))
   :middleware wrap-dev})
