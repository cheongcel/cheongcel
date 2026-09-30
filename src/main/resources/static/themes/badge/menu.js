/* badge · 호버 메뉴
 * - 위쪽 메뉴줄(.nav-links)에 마우스를 대면 화면 전체가 흐려지며 전체 메뉴가 열려요.
 * - 메뉴 영역 밖으로 마우스가 나가거나, 배경을 누르거나, Esc 를 누르면 닫혀요.
 * - 모바일/터치는 'Menu' 버튼으로 열고 닫아요.
 */
(() => {
  const menu  = document.getElementById('bh-menu');
  const inner = document.getElementById('bh-menu-inner');
  const nav   = document.querySelector('.nav');
  const links = document.querySelector('.nav-links');
  if (!menu || !nav) return;

  let closeTimer = null;
  const stop  = () => { clearTimeout(closeTimer); closeTimer = null; };
  const open  = () => { stop(); menu.classList.add('open');    menu.setAttribute('aria-hidden', 'false'); };
  const close = () => { stop(); menu.classList.remove('open'); menu.setAttribute('aria-hidden', 'true'); };

  // 스크롤하면 내비에 배경이 깔리게
  const onScroll = () => nav.classList.toggle('bh-scrolled', window.scrollY > 40);
  onScroll(); window.addEventListener('scroll', onScroll, { passive: true });

  // 모바일용 Menu 버튼
  const btn = document.createElement('button');
  btn.type = 'button'; btn.className = 'bh-menu-btn'; btn.textContent = 'Menu';
  btn.setAttribute('aria-expanded', 'false'); btn.setAttribute('aria-controls', 'bh-menu');
  nav.appendChild(btn);
  btn.addEventListener('click', () => { menu.classList.contains('open') ? close() : open(); });
  new MutationObserver(() => btn.setAttribute('aria-expanded', menu.classList.contains('open'))).observe(menu, { attributes: true, attributeFilter: ['class'] });

  // 데스크톱: 메뉴줄에 호버하면 열기
  if (links) {
    links.addEventListener('mouseenter', open);
    links.addEventListener('focusin', open);
  }

  // 마우스가 (내비 메뉴줄 + 큰 메뉴 영역) 근처를 벗어나면 잠깐 뒤 닫기
  const inflate = (r, m) => ({ l: r.left - m, r: r.right + m, t: r.top - m, b: r.bottom + m });
  const inside  = (x, y, o) => x >= o.l && x <= o.r && y >= o.t && y <= o.b;
  window.addEventListener('pointermove', e => {
    if (!menu.classList.contains('open') || e.pointerType === 'touch') return;
    const zones = [inflate(nav.getBoundingClientRect(), 4), inflate(inner.getBoundingClientRect(), 50)];
    const isIn = zones.some(z => inside(e.clientX, e.clientY, z));
    if (isIn) stop();
    else if (!closeTimer) closeTimer = setTimeout(() => { closeTimer = null; close(); }, 280);
  });
  menu.addEventListener('click', e => { if (!e.target.closest('a')) close(); });
  document.addEventListener('keydown', e => { if (e.key === 'Escape') close(); });
  document.addEventListener('mouseleave', close);
})();
