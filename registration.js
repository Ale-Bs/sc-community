function enviarExito() {
  const msg = document.getElementById("successMessage");

  if (msg) {
    msg.style.display = "block";
    setTimeout(() => {
      msg.style.display = "none";
    }, 3000);
  }
}

function configurarNavegacionPaginas() {
  const nav = document.querySelector("header nav");
  if (!nav) return;

  const enlaces = nav.querySelectorAll("a");

  if (enlaces[0]) enlaces[0].href = "index.html";
  if (enlaces[1]) enlaces[1].href = "torneos.html";
  if (enlaces[2]) enlaces[2].href = "servidores.html";
  if (enlaces[3]) enlaces[3].href = "historial.html";

  const tarjetaTorneos = document.querySelector(".tournament-card .card-action");
  if (tarjetaTorneos) tarjetaTorneos.href = "torneos.html";

  const botonHistorial = document.querySelector(".champions-panel .panel-action");
  if (botonHistorial) botonHistorial.href = "historial.html";
}

document.addEventListener("DOMContentLoaded", configurarNavegacionPaginas);
