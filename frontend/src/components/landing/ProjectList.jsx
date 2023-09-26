import { Navigation, Pagination, Scrollbar, A11y } from 'swiper'

import { Swiper, SwiperSlide } from 'swiper/react'

// import 'swiper/css'
// import 'swiper/css/navigation'
// import 'swiper/css/pagination'
// import 'swiper/css/scrollbar'

const ProjectList = () => {
    return (
        <Swiper
            modules={[Navigation, Pagination, Scrollbar, A11y]}
            spaceBetween={50}
            slidesPerView={3}
            navigation
            pagination={{ clickable: true }}
            scrollbar={{ draggable: true }}
            onSwiper={swiper => console.log(swiper)}
            onSlideChange={() => console.log('slide change')}>
            <SwiperSlide>
                <img src={require('images/thread_profile.png').default} alt={`스레드 이미지`} />
            </SwiperSlide>
            <SwiperSlide>
                <img src={require('images/thread_profile.png').default} alt={`스레드 이미지`} />
            </SwiperSlide>
            <SwiperSlide>
                <img src={require('images/thread_profile.png').default} alt={`스레드 이미지`} />
            </SwiperSlide>
            <SwiperSlide>Slide 4</SwiperSlide>
            ...
        </Swiper>
    )
}

export default ProjectList
