(ns clj-guestbook.env
  (:require [clojure.tools.logging :as log]))

(def defaults
  {:init
   (fn []
     (log/info "\n-=[clj-guestbook started successfully]=-"))
   :stop
   (fn []
     (log/info "\n-=[clj-guestbook has shut down successfully]=-"))
   :middleware identity})
