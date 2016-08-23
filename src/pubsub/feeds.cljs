(ns pubsub.feeds
  (:require-macros
    [cljs.core.async.macros :refer [go-loop]])
  (:require [cljs.core.async :refer [chan pub sub <! put! close!]])
  )

;;;
;; Define an event bus carrying messages [topic message]
;;
;; Use this to write new events
;;;

(defrecord Feed [input output])

(defn create-feed []
  "Create a new feed"
  (let [in-chan (chan)]
    (->Feed in-chan (pub in-chan first))))

(defprotocol TopicFeed
  (publish
    [_ message]
    "write a message to the feed for distribution, returning false if the feed is closed")
  (subscribe
    [_ handler]
    "subscribe to this feed, passing topic & messages to the handler"))

(defrecord Topic [topic feed]
  TopicFeed
  (publish [_ message]
    (put! (:input feed) [topic message]))
  (subscribe [_ handler]
    (let [in-chan (chan)]
      (sub (:output feed) topic in-chan)
      (go-loop []
        (let [[topic-key message] (<! in-chan)]
          (if (= message :close)
            (do
              ;(close! (:input feed))
              (close! in-chan))
            (do
              (handler topic-key message)
              (recur))))))))

(defn close [topic feed]
  "Call this function on any topic for each open feed when reloading software.
  e.g. in figwheel onload. It places a message
  on the :close topic, causing all subscriptions to close."
  []
  (publish topic :close))
