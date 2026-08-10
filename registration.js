const REGISTRATION_API_URL="https://script.google.com/macros/s/AKfycbyvbLz9oi3BFn3jMx-25wGHntHmJEqzMHaOrGqr2LNWlUX3kFzlQWeijRwF3EAgs2fD/exec";

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
  }finally{
    contadorActualizando=false;
  }
}

function enviarExito(){
  const msg=document.getElementById("successMessage");
  if(msg){
    msg.style.display="block";
    setTimeout(()=>msg.style.display="none",3000);
  }

  setTimeout(actualizarContadorRegistro,1500);
  setTimeout(actualizarContadorRegistro,4000);
}

document.addEventListener("DOMContentLoaded",()=>{
  actualizarContadorRegistro();
  setInterval(actualizarContadorRegistro,10000);
});
