(ns pubsubcards.devcards
  (:require
            [cljs.test :refer-macros [is testing async]]
            [pubsubcards.core :as core]
            [pubsub.feeds :as feeds]
            [cljs.core.async :refer [<! timeout close!]]
            [devcards.core :refer-macros [defcard deftest]]
            )
  (:require-macros
    [cljs.core.async.macros :refer [go]]))

(enable-console-print!)

(def async-result (atom nil))

(defcard
  (core/rum-tester))

(deftest read-write
  "### subscribe and publish"
  (testing "subscribe once"
    (let [feed (feeds/create-feed)
          topic (feeds/->Topic :greetings feed)]
      (feeds/subscribe topic #(reset! async-result (str (name %1) ": " %2)))
      (feeds/publish topic "Hello there!")
      )
    (async done
      (go
        (<! (timeout 100))
        (is (= @async-result "greetings: Hello there!") "read then write once")
        (done)))
    )
  (testing "subscribe once"
    (let [feed (feeds/create-feed)
          topic (feeds/->Topic :greetings feed)]
      (feeds/publish topic "Hello there!")
      (feeds/subscribe topic #(reset! async-result (str (name %1) ": " %2)))
      )
    (async done
      (go
        (<! (timeout 100))
        (is (= @async-result "greetings: Hello there!") "write once then read")
        (done)))
    )
  (testing "publish twice"
    (let [feed (feeds/create-feed)
          topic (feeds/->Topic :greetings feed)]
      (reset! async-result "")
      (feeds/subscribe topic (fn [key msg] (swap! async-result #(str %1
                                                                     (if (= %1 "") "" " | ")
                                                                     (str (name key) ": " msg)))))
      (feeds/publish topic "Hello there 1!")
      (feeds/publish topic "Hello there 2!")
      )
    (async done
      (go
        (<! (timeout 100))
        (is (= @async-result "greetings: Hello there 1! | greetings: Hello there 2!") "write twice")
        (done)))
    )
  (testing "subscribe to 2 feeds"
    (let [feed1 (feeds/create-feed)
          feed2 (feeds/create-feed)
          topic1 (feeds/->Topic :greetings feed1)
          topic2 (feeds/->Topic :salutations feed2)]
      (letfn [(collect [key msg] (swap! async-result #(str %1
                                                           (if (= %1 "") "" " | ")
                                                           (str (name key) ": " msg))))]
        (reset! async-result "")
        (feeds/subscribe topic1 collect)
        (feeds/subscribe topic2 collect)
        (feeds/publish topic1 "Hello there!")
        (feeds/publish topic2 "Hi!"))
      )
    (async done
      (go
        (<! (timeout 100))
        (is (= @async-result "greetings: Hello there! | salutations: Hi!") "subscribe to 2 feeds")
        (done)))
    )
  (testing "subscribe to 1 feed 3 times"
    (let [feed (feeds/create-feed)
          topic (feeds/->Topic :greetings feed)]
      (letfn [(collect [key msg] (swap! async-result #(str %1
                                                           (if (= %1 "") "" " | ")
                                                           (str (name key) ": " msg))))]
        (reset! async-result "")
        (feeds/subscribe topic collect)
        (feeds/subscribe topic collect)
        (feeds/subscribe topic collect)
        (feeds/publish topic "Hello there 1!")
        )
      )
    (async done
      (go
        (<! (timeout 100))
        (is (= @async-result "greetings: Hello there 1! | greetings: Hello there 1! | greetings: Hello there 1!") "subscribe to 1 feed 3 times")
        (done)))
    )

  (testing "close a feed"
    (let [feed (feeds/create-feed)
          topic (feeds/->Topic :greetings feed)]
      (letfn [(collect [key msg] (swap! async-result #(str %1
                                                           (if (= %1 "") "" " | ")
                                                           (str (name key) ": " msg))))]
        (reset! async-result "")
        (feeds/subscribe topic collect)
        (feeds/publish topic "Hello there 1!")
        (feeds/close topic feed)
        )

      (async done
        (go
          (<! (timeout 100))
          (is (= @async-result "greetings: Hello there 1!") "close a feed")
          (reset! async-result "")
          (is (= @async-result "") "feed remains closed")
          )
        (go
          (<! (timeout 200))
          (done))
        ))
    )
  )

(deftest pubsub-tests
  "### Pubsub"
  (testing "feed creation:"
    (is (:input (feeds/create-feed)) "created feed has input channel")
    (is (:output (feeds/create-feed)) "and output channel")
    )
  )

(deftest check-tests
  "### Example tests"
  (testing "checking tests"
    (is (= 2 2) "should succeed")
    (is (= 1 2) "should fail")))


