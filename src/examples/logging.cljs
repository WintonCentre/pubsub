(ns examples.logging
  (:require [pubsub.feeds :refer [create-feed ->Topic subscribe publish]]))

(enable-console-print!)

(def feed (create-feed))

(def warning (->Topic :warning feed))
(def severe (->Topic :error feed))


(defn log-it [topic message] (println topic " " message))

(subscribe warning log-it)
(subscribe severe log-it)

(publish warning "A warning message")
(publish severe "Something bad happened")