// cheongcel main.js

// 페이지 로드 시 nav active 상태
document.addEventListener('DOMContentLoaded', () => {
    const path = window.location.pathname;
    document.querySelectorAll('.nav-links a').forEach(link => {
        if (link.getAttribute('href') !== '/' && path.startsWith(link.getAttribute('href'))) {
            link.classList.add('active');
        }
    });
});
