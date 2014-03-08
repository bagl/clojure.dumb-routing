(ns dumb-routing.util)

(defn find-route [routes uri]
  (->> routes
       (filter #(re-matches (first %) uri))
       first))

(defn build-handler [route uri]
  (let [route-regex (first route)
        handler (second route)
        match (re-matches route-regex uri)
        matched-uri-parts (if (coll? match) (rest match) nil)]
    (if matched-uri-parts
      (apply handler matched-uri-parts)
      handler)))
