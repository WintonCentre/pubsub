(ns pubsubcards.core
    (:require
     [rum.core :as rum]
     [cljs.test :as t]
     ))

(enable-console-print!)

;; Visit http://localhost:3449/index.html to see this

(rum.core/defc rum-tester []
  [:div
   [:h1 "Pubsub test cards"]]
  )

(defn main []
  ;; conditionally start the app based on whether the #app
  ;; node is on the page
  (if-let [node (.getElementById js/document "app")]
    (rum/mount (rum-tester) node)))

(main)
