(ns todo.core
  (:require [clojure.java.io :as io]
            [io.pedestal.http :as http]
            [io.pedestal.http.route :as route :refer [router]]
            [io.pedestal.http.route.definition :refer [defroutes]]
            [ns-tracker.core :refer [ns-tracker]]
            [ring.util.response :as ring-resp]))

(defn home-page
  ([req]
   (let [gif (io/file (io/resource "public/under-construction.gif"))]
     (assoc-in (ring-resp/response gif) [:headers "Content-Type"]
               "image/gif"))))

(defroutes routes
  [[["/"
     {:get home-page}]]])

(def track-namespaces
  (ns-tracker ["src"]))

(def service {::http/interceptors [http/log-request
                                   http/not-found
                                   (router (fn []
                                             (doseq [ns-sym (track-namespaces)]
                                               (require ns-sym :reload))
                                             routes))]
              ::http/port 8080})

(defn -main
  [& args]
  (-> service
      http/create-server
      http/start))
