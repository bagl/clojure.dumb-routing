(ns dumb-routing.core
  (:require [dumb-routing.util :refer [not-found-handler
                                       find-route
                                       build-handler]]))

(defn basic-handler [routes & [custom-not-found]]
  (fn [request]
    (let [uri (:uri request)
          not-found (or custom-not-found not-found-handler)
          route (find-route routes uri)
          handler (if route (build-handler route uri) not-found)]
        (handler request))))
