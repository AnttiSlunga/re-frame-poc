(defproject re-frame-poc "0.1.0-SNAPSHOT"
  :dependencies [[org.clojure/clojure        "1.10.0"]
                 [org.clojure/clojurescript  "1.10.439"]
                 [reagent  "0.8.1"]
                 [re-frame "0.10.6"]]

  :plugins [[lein-cljsbuild "1.1.7"]
            [lein-figwheel "0.5.18"]]

  :profiles {:dev {:cljsbuild
                   {:builds {:client {:figwheel     {:on-jsload "rfp.core/run"}
                                      :compiler     {:main "rfp.core"
                                                     :asset-path "js"
                                                     :optimizations :none
                                                     :source-map true
                                                     :source-map-timestamp true}}}}}

             :prod {:cljsbuild
                    {:builds {:client {:compiler    {:optimizations :advanced
                                                     :elide-asserts true
                                                     :pretty-print false}}}}}}

  :figwheel {:repl false}

  :clean-targets ^{:protect false} ["resources/public/js"]

  :cljsbuild {:builds {:client {:source-paths ["src"]
                                :compiler     {:output-dir "resources/public/js"
                                               :output-to  "resources/public/js/client.js"}}}})
