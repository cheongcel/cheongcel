/* badge · 양쪽 끈에 걸린 네임택 물리 (verlet 끈 2줄 + 진자 카드)
 * - 카드를 잡고 끌었다 놓으면 흔들리고, 마우스로 스치기만 해도 달랑거려요.
 * - 조절: 아래 TUNE 값만 바꾸면 됩니다.
 */
(() => {
  const root  = document.getElementById('bh');
  if (!root) return;
  const badge = document.getElementById('bh-badge');
  const cv    = document.getElementById('bh-rope');
  const title = document.getElementById('bh-title');
  const hint  = document.getElementById('bh-hint');
  const ctx   = cv.getContext('2d');

  const TUNE = {
    N: 16,              // 끈 한 줄을 이루는 점 개수
    anchorX: 0.22,      // 끈이 시작되는 좌우 위치 (화면 폭 비율, 작을수록 더 바깥)
    hookY: 0.36,        // 카드가 걸리는 높이 (히어로 높이 비율)
    slack: 0.98,         // 끈 여유 (1.0 = 팽팽)
    strapWidth: 15,
    strapColor: '#1d1f1e',
    gravity: 0.06,       // 끈 자체 무게
    cardGravity: 0.9,    // 카드가 끈을 잡아당기는 무게 (끈보다 훨씬 커야 팽팽한 V자)
    hoverPush: 0.0001,  // 마우스로 스칠 때 흔들리는 세기 (0 이면 잡아끌 때만 움직임)
    air: 0.992
  };

  const HW = 30;       // 카드 위로 올라오는 금속 고리 높이 (theme.css 의 .bh-hw 와 동일)
  let W, H, dpr, cardW, cardH;
  let A = [], B = [], hook, segA = 10, segB = 10;
  let theta = 0, omega = 0;
  let drag = null;
  const mouse = { x: 0, y: 0, vx: 0, seen: false };

  const pt = (x, y) => ({ x, y, px: x, py: y });

  function fitTitle() {
    const s = title.firstElementChild;
    title.style.fontSize = '100px';
    const w = s.getBoundingClientRect().width || 1;
    title.style.fontSize = (100 * (root.clientWidth * 1.04) / w) + 'px';
  }

  function setup() {
    dpr = window.devicePixelRatio || 1;
    W = root.clientWidth; H = root.clientHeight;
    cv.width = W * dpr; cv.height = H * dpr;
    cv.style.width = W + 'px'; cv.style.height = H + 'px';
    ctx.setTransform(dpr, 0, 0, dpr, 0, 0);
    cardW = badge.offsetWidth; cardH = badge.offsetHeight;

    const ax = W < 640 ? 0.04 : TUNE.anchorX, N = TUNE.N;
    const aL = { x: W * ax, y: -30 }, aR = { x: W * (1 - ax), y: -30 };
    const hx = W / 2, hy = Math.min(H * TUNE.hookY, 330);

    hook = pt(hx, hy);
    const build = (a) => {
      const len = Math.hypot(hx - a.x, hy - a.y) * TUNE.slack;
      const seg = len / (N - 1), arr = [];
      for (let i = 0; i < N - 1; i++) {
        const t = i / (N - 1);
        arr.push(pt(a.x + (hx - a.x) * t, a.y + (hy - a.y) * t));
      }
      arr.push(hook);
      return { arr, seg };
    };
    const l = build(aL), r = build(aR);
    A = l.arr; segA = l.seg; B = r.arr; segB = r.seg;
    theta = 0; omega = 0;
    fitTitle();
  }

  function solve(chain, seg, anchor) {
    const N = chain.length;
    chain[0].x = anchor.x; chain[0].y = anchor.y;
    for (let i = 0; i < N - 1; i++) {
      const a = chain[i], b = chain[i + 1];
      const dx = b.x - a.x, dy = b.y - a.y;
      const d = Math.hypot(dx, dy) || 1e-4;
      const diff = (d - seg) / d;
      if (i === 0) { b.x -= dx * diff; b.y -= dy * diff; }
      else { a.x += dx * diff * .5; a.y += dy * diff * .5; b.x -= dx * diff * .5; b.y -= dy * diff * .5; }
    }
    // 직조 끈은 너무 뾰족하게 꺾이지 않아요: 두 칸 건너 점이 너무 가까워지면 살짝 밀어냄
    for (let i = 1; i < N - 1; i++) {
      const a = chain[i - 1], b = chain[i + 1];
      const dx = b.x - a.x, dy = b.y - a.y, d = Math.hypot(dx, dy) || 1e-4, min = seg * 1.8;
      if (d < min) {
        const k = (min - d) / d * .22;
        if (i - 1 > 0) { a.x -= dx * k; a.y -= dy * k; }
        if (i + 1 < N - 1) { b.x += dx * k; b.y += dy * k; }
      }
    }
  }

  function step() {
    const N = TUNE.N, G = TUNE.gravity;
    const hvx0 = hook.x - hook.px;
    const free = [];
    for (let i = 1; i < N; i++) free.push(A[i]);
    for (let i = 1; i < N - 1; i++) free.push(B[i]);
    free.forEach(p => {
      const g = p === hook ? TUNE.cardGravity : G;
      const vx = (p.x - p.px) * TUNE.air, vy = (p.y - p.py) * TUNE.air;
      p.px = p.x; p.py = p.y;
      p.x += vx; p.y += vy + g;
    });

    const aL = A[0], aR = B[0];
    if (drag) {
      let tx = mouse.x - drag.dx, ty = mouse.y - drag.dy;
      const RL = (N - 1) * segA * .98, RR = (N - 1) * segB * .98;
      for (let k = 0; k < 3; k++) {
        [[aL, RL], [aR, RR]].forEach(([an, R]) => {
          const ddx = tx - an.x, ddy = ty - an.y, dd = Math.hypot(ddx, ddy);
          if (dd > R) { tx = an.x + ddx / dd * R; ty = an.y + ddy / dd * R; }
        });
      }
      let mx = (tx - hook.x) * .25, my = (ty - hook.y) * .25;
      const m = Math.hypot(mx, my); if (m > 22) { mx *= 22 / m; my *= 22 / m; }
      hook.x += mx; hook.y += my;
    }
    const axL = { x: aL.x, y: aL.y }, axR = { x: aR.x, y: aR.y };
    for (let k = 0; k < 60; k++) { solve(A, segA, axL); solve(B, segB, axR); }

    // 카드 = 걸이점을 축으로 하는 진자
    const L = cardH * .55 + HW;
    const hax = Math.max(-4, Math.min(4, (hook.x - hook.px) - hvx0));
    let alpha = -(.55 / L) * Math.sin(theta) * 9 - (hax / L) * Math.cos(theta) * .9;
    if (drag) alpha -= theta * .05;
    omega = (omega + alpha) * .972;
    theta += omega;
    if (Math.abs(theta) > 1.1) { theta = Math.sign(theta) * 1.1; omega *= -.3; }
  }

  // ---- 실제 라니야드(직물 끈)처럼: 그림자 · 가장자리 · 직조 결 · 박음질 ----
  function path(P) {
    ctx.beginPath();
    ctx.moveTo(P[0].x, P[0].y);
    for (let i = 1; i < P.length - 1; i++) {
      const xc = (P[i].x + P[i + 1].x) / 2, yc = (P[i].y + P[i + 1].y) / 2;
      ctx.quadraticCurveTo(P[i].x, P[i].y, xc, yc);
    }
    ctx.lineTo(P[P.length - 1].x, P[P.length - 1].y);
  }
  function offset(P, d) {
    return P.map((p, i) => {
      const a = P[Math.max(0, i - 1)], b = P[Math.min(P.length - 1, i + 1)];
      const dx = b.x - a.x, dy = b.y - a.y, l = Math.hypot(dx, dy) || 1;
      return { x: p.x - dy / l * d, y: p.y + dx / l * d };
    });
  }
  function strap(chain) {
    const P = chain.map(p => ({ x: p.x, y: p.y }));
    P[P.length - 1].y += 8;                       // 끝은 금속 크림프 속으로 들어감
    const w = TUNE.strapWidth;
    ctx.lineCap = 'butt'; ctx.lineJoin = 'round';

    ctx.save();                                    // 1) 바닥에 드리우는 그림자
    ctx.shadowColor = 'rgba(0,0,0,.30)'; ctx.shadowBlur = 9; ctx.shadowOffsetY = 6;
    path(P); ctx.strokeStyle = '#14161a'; ctx.lineWidth = w; ctx.stroke();
    ctx.restore();

    path(P); ctx.strokeStyle = '#1b1e22'; ctx.lineWidth = w; ctx.stroke();            // 2) 가장자리(어두움)
    path(P); ctx.strokeStyle = '#2a2e33'; ctx.lineWidth = w - 2.6; ctx.stroke();      //    면
    path(P); ctx.strokeStyle = 'rgba(255,255,255,.07)'; ctx.lineWidth = w * .38; ctx.stroke(); // 중앙 은은한 광택

    ctx.save();                                    // 3) 직조 결: 끈 방향에 수직인 짧은 줄무늬
    ctx.setLineDash([1.1, 2.3]);
    path(P); ctx.strokeStyle = 'rgba(255,255,255,.085)'; ctx.lineWidth = w - 3; ctx.stroke();
    ctx.lineDashOffset = 1.7;
    path(P); ctx.strokeStyle = 'rgba(0,0,0,.18)'; ctx.stroke();
    ctx.restore();

    ctx.save();                                    // 4) 양쪽 박음질
    ctx.setLineDash([3.2, 2.6]); ctx.lineWidth = .9; ctx.strokeStyle = 'rgba(214,219,225,.55)';
    [-1, 1].forEach(s => { path(offset(P, s * (w / 2 - 2.3))); ctx.stroke(); });
    ctx.restore();
  }

  function draw() {
    ctx.clearRect(0, 0, W, H);
    strap(A); strap(B);
    badge.style.transform = `translate(${hook.x - cardW / 2}px, ${hook.y + HW}px) rotate(${theta}rad)`;
  }

  const local = e => { const r = root.getBoundingClientRect(); return { x: e.clientX - r.left, y: e.clientY - r.top }; };

  badge.addEventListener('pointerdown', e => {
    e.preventDefault();
    badge.setPointerCapture(e.pointerId);
    const p = local(e);
    mouse.x = p.x; mouse.y = p.y;
    drag = { dx: p.x - hook.x, dy: p.y - hook.y };
    badge.classList.add('bh-grab');
    hint.style.opacity = 0;
  });
  window.addEventListener('pointermove', e => {
    const p = local(e);
    mouse.vx = Math.max(-40, Math.min(40, p.x - mouse.x));
    if (!mouse.seen) { mouse.vx = 0; mouse.seen = true; }
    mouse.x = p.x; mouse.y = p.y;
    if (drag) return;
    const r = badge.getBoundingClientRect();   // 카드 위를 스치면 살짝 밀림
    if (e.clientX > r.left && e.clientX < r.right && e.clientY > r.top && e.clientY < r.bottom) {
      omega += mouse.vx * TUNE.hoverPush;     // 살짝 스칠 때만 가볍게 달랑
    }
  });
  const release = () => { if (!drag) return; drag = null; badge.classList.remove('bh-grab'); };
  window.addEventListener('pointerup', release);
  window.addEventListener('pointercancel', release);
  window.addEventListener('resize', setup);

  const reduce = window.matchMedia && window.matchMedia('(prefers-reduced-motion: reduce)').matches;
  (document.fonts && document.fonts.ready ? document.fonts.ready : Promise.resolve()).then(() => {
    setup();
    badge.style.visibility = 'visible';
    if (!reduce) { omega = .012; }                      // 처음 로드될 때 아주 살짝만
    (function loop() { step(); draw(); requestAnimationFrame(loop); })();
    setTimeout(() => { hint.style.opacity = 0; }, 6000);
  });
})();
