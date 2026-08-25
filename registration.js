const REGISTRATION_API_URL="https://script.google.com/macros/s/AKfycbyOA38kueRnH1V1VhOn1IGBliGxKUbwAnhXEBoPooAT6tni8k5o9VL-0LWE7uJX-Ond/exec";

let contadorActualizando=false;

async function actualizarContadorRegistro(){
  const el=document.getElementById("contador");
  if(!el||contadorActualizando)return;
  contadorActualizando=true;
  try{
    const url=`${REGISTRATION_API_URL}?t=${Date.now()}`;
    const res=await fetch(url,{method:"GET",cache:"no-store",credentials:"omit"});
    if(!res.ok)throw new Error(`HTTP ${res.status}`);
    const data=await res.json();
    const inscritos=Number(data.inscritos);
    if(!Number.isFinite(inscritos))throw new Error("Respuesta inválida del contador");
    el.textContent=inscritos;
    el.title="Contador actualizado";
  }catch(e){
    console.error("Error contador:",e);
    el.textContent="--";
    el.title="No se pudo conectar con el contador";
  }finally{contadorActualizando=false;}
}

function enviarExito(){
  const msg=document.getElementById("successMessage");
  if(msg){msg.style.display="block";setTimeout(()=>msg.style.display="none",3000);}
  setTimeout(actualizarContadorRegistro,1500);
  setTimeout(actualizarContadorRegistro,4000);
}

function configurarNavegacionPaginas(){
  const nav=document.querySelector("header nav");
  if(!nav)return;
  const enlaces=nav.querySelectorAll("a");
  // IMPORTANTE: Inicio debe llevar al inicio real de index.html.
  // No usar #inicio aquí porque el sistema de Registro también usa ese hash.
  if(enlaces[0])enlaces[0].href="index.html";
  if(enlaces[1])enlaces[1].href="torneos.html";
  if(enlaces[2])enlaces[2].href="servidores.html";
  if(enlaces[3])enlaces[3].href="historial.html";

  const tarjetaTorneos=document.querySelector(".tournament-card .card-action");
  if(tarjetaTorneos)tarjetaTorneos.href="torneos.html";
  const botonHistorial=document.querySelector(".champions-panel .panel-action");
  if(botonHistorial)botonHistorial.href="historial.html";
}

document.addEventListener("DOMContentLoaded",()=>{
  actualizarContadorRegistro();
  setInterval(actualizarContadorRegistro,10000);
  configurarNavegacionPaginas();
});
