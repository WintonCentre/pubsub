 (ns pubsub.success-test
   (:require [cljs.test :refer-macros [deftest is testing run-tests]]
             [pubsub.feeds :as ps]))

(enable-console-print!)


(deftest test-numbers
   (is (= 1 1)))

 (run-tests)