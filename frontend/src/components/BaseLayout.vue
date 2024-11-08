<script setup>

import logoImage from "@/img/tren.jpg";
import bicycleImage from "@/img/bicycle.png";
</script>

<template>
  <div>
    <header>
      <div class="mobileHeader">
        <div class="logo-and-icon">
          <a class="logo-link" href="">
            <img class="logo" :src="logoImage" alt="tren" />
          </a>

          <div class="icon-container">
            <img :src="bicycleImage" alt="bicycle" />
          </div>
        </div>
        <div class="dropdown">
          <div class="dropdown-child">
            <a href="">Головна</a>
            <a href="">Авторизація</a>
            <a href="">Калькулятор</a>
            <a href="">Питання та відповіді</a>
          </div>
          <div class="hamburger-icon">
            <button class="hamburger">☰</button>
          </div>
        </div>
      </div>
    </header>
    <main>
      <slot/>
    </main>
    <footer>
      <div class="footer-content">
        <p>Будьте з нами на зв'язку</p>
        <div class="footer-icons">
          <a href="tel:+380123456789" class="icon-link" title="Подзвонити">
            <i class="fas fa-phone"></i>
          </a>
          <a href="mailto:info@sportlife.com" class="icon-link" title="Відправити Email">
            <i class="fas fa-envelope"></i>
          </a>
          <a href="https://t.me/yourtelegram" class="icon-link" title="Чат у Telegram">
            <i class="fab fa-telegram"></i>
          </a>
          <a href="https://wa.me/380123456789" class="icon-link" title="Чат у WhatsApp">
            <i class="fab fa-whatsapp"></i>
          </a>
          <a href="viber://chat?number=%2B380123456789" class="icon-link" title="Чат у Viber">
            <i class="fab fa-viber"></i>
          </a>
        </div>
      </div>
      <div class="copyright">© 2007-2024 Group of companies TREN</div>
    </footer>
  </div>
</template>

<style src="../css/registration.css"></style>
<style src="../css/hoverFooter.css"></style>
<style src="../css/authorizationAndRegistrationStyles.css"></style>