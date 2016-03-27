(defproject todo "0.1.0-SNAPSHOT"
  :description "Will code for democracy!"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [io.pedestal/pedestal.jetty "0.4.1"]
                 [io.pedestal/pedestal.service "0.4.1"]
                 [io.pedestal/pedestal.service-tools "0.4.1"]
                 [ns-tracker "0.3.0"]]
  :main ^{:skip-aot true} todo.core)
