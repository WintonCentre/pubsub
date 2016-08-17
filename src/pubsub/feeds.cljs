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
(def feed-input (chan))

;;;
;; Define a topic feed using pub.
;;;
(def feed-output (pub feed-input first))

(def feeds {:events {:input feed-input
                     :output feed-output}})

(defrecord Feed [input output])

(defprotocol TopicFeed
  (publish [_ message])
  (subscribe [_ handler]))

(defrecord Topic [topic feed]
  TopicFeed
  (publish [_ message]
    (put! (:input feed) [topic message]))
  (subscribe [_ handler]
    (let [in-chan (chan)]
      (sub (:output feed) topic in-chan)
      (go-loop []
        (let [[topic-key message] (<! in-chan)]
          (if (= topic-key :reloading)
            (close! in-chan)
            (do (handler topic-key message)
                (recur))))))))

(defn publish* [topic message]
  "publish a message on a topic (usually a keyword) "
  (put! feed-input [topic message]))

(defn subscribe* [topic handler]
  "subscribe to a topic (keyword), and handle its messages with the handler.
  The handler is passed both the topic and message in a vector [topic message]."
  (let [in-chan (chan)]
    (sub feed-output topic in-chan)
    (go-loop []
      (let [[topic-key _ :as notification] (<! in-chan)]
        (if (= topic-key :reloading)
          (close! in-chan)
          (do (handler notification)
              (recur)))))))

(defn reloading
  "Call this function when reloading software e.g. in figwheel onload. It places a message
  on the :reloading topic, causing all subscriptions to close."
  []
  (put! feed-input [:reloading nil])
  )
