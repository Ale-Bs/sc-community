const CACHE_NAME = "sc-latam-community-v6";

const FILES_TO_CACHE = [
  "./",
  "./index.html",
  "./eventos.html",
  "./torneos.html",
  "./servidores.html",
  "./historial.html",
  "./style.css",
  "./panel-headers.css",
  "./mobile.css",
  "./registration.css",
  "./registration.js",
  "./manifest.json",
  "./logo.png"
];

self.addEventListener("install", (event) => {
  self.skipWaiting();
  event.waitUntil(
    caches.open(CACHE_NAME).then((cache) => cache.addAll(FILES_TO_CACHE))
  );
});

self.addEventListener("activate", (event) => {
  event.waitUntil(
    caches.keys().then((keys) =>
      Promise.all(
        keys.filter((key) => key !== CACHE_NAME).map((key) => caches.delete(key))
      )
    ).then(() => self.clients.claim())
  );
});

self.addEventListener("fetch", (event) => {
  if (event.request.method !== "GET") return;
  event.respondWith(fetch(event.request));
});
