(ns pubsub.test-runner
  (:require [pubsub.failing-test]
            [pubsub.success-test]
            [cljs.test :refer-macros [deftest is run-tests]]))

(enable-console-print!)


(defn ^:export run []
  (prn "calling run")
  ;  (run-tests)
  )
