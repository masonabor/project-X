<template>
  <div>
    <!-- Video Section -->
    <section id="video">
      <video src="../public/videos/testosterone_boost.mp4" autoplay loop muted></video>
    </section>

    <!-- Our Clients Section -->
    <section class="our-clients">
      <div class="our-clients-container">
        <h2 style="text-align: center;">Наші клієнти</h2>
        <p class="our-clients__text">
          Lorem ipsum dolor sit amet, consectetur adipiscing elit. Orci odio amet consectetur volutpat sodales a lectus
          a. Et gravida tincidunt morbi habitasse. Ultrices magna laoreet ornareLorem ipsum dolor sit amet, consectetur
          adipiscing elit. Orci odio amet consectetur volutpat sodales a lectus a. Et gravida tincidunt morbi
          habitasse
        </p>
        <div class="our-clients-photos">
          <img src="../public/img/professor.jpg" alt="error" />
          <img src="../public/img/tren_twins.jpg" alt="error" />
          <img src="../public/img/exe.webp" alt="error" />
          <img src="../public/img/cutler.jpeg" alt="error" />
        </div>
      </div>
    </section>

    <!-- Our Services Section -->
    <section id="our-services">
      <h2>Наші послуги</h2>
      <h1>Популярні послуги</h1>

      <div class="swiper-wrapper-container">
        <div class="swiper-container">
          <div class="swiper-wrapper">
            <div class="swiper-slide" v-for="(service, index) in services" :key="index">
              <img :src="service.image" :alt="`Service ${index + 1}`" />
              <h3>{{ service.title }}</h3>
              <p>{{ service.description }}</p>
              <a href="">
                <button class="red-button">Дізнатись більше</button>
              </a>
            </div>
          </div>
        </div>

        <div class="swiper-navigation">
          <div class="swiper-button-next"></div>
          <div class="swiper-button-prev"></div>
        </div>
      </div>
    </section>

    <!-- About Us Section -->
    <section id="about-us">
      <div class="container">
        <aside id="map" style="flex: 1;">
          <img src="../public/img/map.png" alt="Карта" />
        </aside>
        <aside id="description">
          <h2>Про нас</h2>
          <h1>Мережа фітнес-клубів №1 в Україні</h1>
          <p>Ласкаво просимо в сім'ю Tren!</p>
          <p>
            Обери клуб, і отримай доступ до необмежених можливостей фітнесу світового стандарту. У тренажерних залах —
            тільки сучасне обладнання зі США та Італії, понад 200 групових тренувань.
          </p>
          <p>
            Спортивні, розважальні, дитячі басейни оснащені багаторівневою системою очищення води, в SPA-комплексах —
            римські парні, українські лазні та фінські сауни, салони масажу, солярії та затишні лаунж-зони.
          </p>
          <p>Обирай trenbolone і ставай #я❤️TREN</p>
          <a href="">
            <button class="red-button">Обрати trenbolon</button>
          </a>
        </aside>
      </div>
    </section>
  </div>
</template>

<script>
import Swiper from "swiper";
import {Navigation} from "swiper/modules";

export default {
  name: "GymWebsite",
  data() {
    return {
      services: [
        {
          image: "../img/aesthetic.jpg",
          title: "Тренажерний зал",
          description: "Сучасні тренажери зі США та Італії",
        },
        {
          image: "../img/sam_sulek.jpg",
          title: "Басейн",
          description: "Сучасні басейни з системою очищення води",
        },
        {
          image: "../img/king.webp",
          title: "Групові заняття",
          description: "Понад 200 групових тренувань на будь-який смак",
        },
        {
          image: "../img/lucky.jpg",
          title: "Персональні тренування",
          description: "Отримайте індивідуальні консультації з тренером",
        },
        {
          image: "../img/sigma.jpg",
          title: "Йога",
          description: "Розслабтесь та знайдіть баланс з нашими йога-класами",
        },
      ],
    };
  },
  mounted() {
    Swiper.use([Navigation]);
    new Swiper(".swiper-container", {
      navigation: {
        nextEl: ".swiper-button-next",
        prevEl: ".swiper-button-prev",
      },
      loop: true,
    });
  },
};
</script>

<style src="../public/css/home.css" scoped>
</style>
