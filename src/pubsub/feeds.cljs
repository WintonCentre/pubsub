(ns pubsub.feeds
  (:require-macros
    [cljs.core.async.macros :refer [go-loop]])
  (:require [cljs.core.async :refer [chan pub sub unsub <! put! close!]])
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

(defn close-feed [feed]
  "Close all topics on this feed, then close the channel.
  Do not re-use a feed once it has been closed. Instead, create a new one."
  (put! (:input feed) [:close "closing"])
  (close! (:input feed)))

(defprotocol TopicFeed
  (publish
    [_ message]
    "write a message to the feed for distribution, returning false if the feed is closed")
  (subscribe
    [_ handler]
    "subscribe to this feed, passing topic & messages to the handler")
  (unsubscribe [_]
    "unsubscribe from this topicfeed"))

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
              (unsub (:output feed) topic in-chan)
              (close! in-chan))
            (do
              (handler topic-key message)
              (recur)))))))
  (unsubscribe [this]
    (publish this :close)))

