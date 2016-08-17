(defproject pubsub "0.1.0-SNAPSHOT"
  :description "Publish Subscribe for messages about topics"
  :url "http://github.com/gmp26/pubsub"
  :license {:name "Eclipse Public License"
            :url  "http://www.eclipse.org/legal/epl-v10.html"}

  :min-lein-version "2.5.3"

  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.clojure/clojurescript "1.8.51"]
                 [org.clojure/core.async "0.2.385"]
                 ]

  :plugins [[lein-cljsbuild "1.1.3"]
            [lein-midje "3.2"]]

  ;:test-paths ["test/pubsub"]

  :cljsbuild {:builds {:minify {:source-paths ["src"]
                                :compiler {:output-to "resources/public/js/main.js"
                                           :output-dir "cljsbuild-output-minify"
                                           :optimizations :advanced
                                           :pretty-print false
                                           :externs ["externs/jquery-externs-1.9.js"
                                                     "externs/externs.js"
                                                     "externs/moment.js"]}}
                       :dev {:source-paths ["src"]
                             :compiler {:output-to "resources/public/js/main.js"
                                        :output-dir "resources/public/js/build-output-dev"
                                        :optimizations :whitespace}}
                       :test {:source-paths ["src" "test"]
                              :compiler {:output-to "resources/public/js/main-test.js"
                                         :optimizations :whitespace
                                         :pretty-print true}}}
              :test-commands {"unit" ["phantomjs"
                                      "resources/test/phantom/runner.js"
                                      "resources/test/test.html"]}} )
