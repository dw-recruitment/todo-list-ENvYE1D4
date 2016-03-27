(ns todo.core
  (:require [clojure.java.io :as io]
            [io.pedestal.http :as http]
            [io.pedestal.http.route :as route :refer [router]]
            [io.pedestal.http.route.definition :refer [defroutes]]
            [ns-tracker.core :refer [ns-tracker]]
            [ring.util.response :as ring-resp]))

(defn resource-response
  "Creates a response for a resource and sets its content-type"
  [resource content-type]
  (let [body (io/file (io/resource resource))]
    (ring-resp/content-type (ring-resp/response body) content-type)))

(defn home-page
  ([req]
   (resource-response "public/under-construction.gif" "image/gif")))

(defn about-page
  ([req]
   (resource-response "public/dev-candidate-exercise.pdf" "application/pdf")))

(defroutes routes
  [[["/"
     {:get home-page}
     ["/about"
      {:get about-page}]]]])

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
