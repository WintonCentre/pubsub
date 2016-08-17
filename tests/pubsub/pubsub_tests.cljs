 (ns pubsub.test-sources
   (:require [cljs.test :refer-macros [deftest is testing run-tests]]
             [pubsub.feeds :as ps]))

 (deftest test-numbers
   (is (= 1 1)))