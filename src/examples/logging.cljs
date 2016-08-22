(ns examples.logging
  (:require [pubsub.feeds :as feeds]))

(enable-console-print!)

(def feed (feeds/create-feed))

(def warning (feeds/->Topic :warning feed))
(def severe (feeds/->Topic :error feed))


(defn log-it [topic message] (println topic " " message))

(feeds/subscribe warning log-it)
(feeds/subscribe severe log-it)

(feeds/publish warning "A warning message")
(feeds/publish severe "Something bad happened")