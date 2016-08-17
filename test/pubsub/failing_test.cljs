(ns pubsub.failing-test
  (:require [cljs.test :refer-macros [deftest is testing run-tests]]
            [pubsub.feeds :as ps]))

(enable-console-print!)


(deftest should-fail
  (is (= 1 1)))

(run-tests)