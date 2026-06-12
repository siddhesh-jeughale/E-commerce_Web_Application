// =====================================================================
// SECTION 1: Active Nav Link Highlighting
// =====================================================================
document.addEventListener('DOMContentLoaded', function () {
    const navLinks = document.querySelectorAll('.nav-link');
    const currentPage = window.location.pathname.split('/').pop();

    navLinks.forEach(link => {
        const href = link.getAttribute('href');
        if (!href) return; // null guard

        const linkPage = href.split('/').pop();

        if (linkPage === currentPage) {
            link.classList.add('active');
        } else {
            link.classList.remove('active');
        }

        link.addEventListener('click', function () {
            navLinks.forEach(item => item.classList.remove('active'));
            this.classList.add('active');
        });
    });
});


// =====================================================================
// SECTION 2: Gallery Page — Filters + Load More
// =====================================================================
document.addEventListener('DOMContentLoaded', function () {

    const filterButtons = document.querySelectorAll('.filter-btn');
    const yearButtons   = document.querySelectorAll('.year-btn');
    const galleryItems  = document.querySelectorAll('.gallery-item-wrapper');
    const loadMoreBtn   = document.querySelector('.load-more-btn');
    const galleryGrid   = document.getElementById('gallery-grid');

    // Only run gallery logic if gallery elements actually exist on this page
    if (!galleryGrid) return;

    let currentFilter = 'all';
    let currentYear   = 'all';
    let currentPage   = 0;
    const pageSize    = 9;

    function filterGallery() {
        galleryItems.forEach(item => {
            const categories    = item.getAttribute('data-category');
            const year          = item.getAttribute('data-year');
            const matchesCategory = currentFilter === 'all' || (categories && categories.includes(currentFilter));
            const matchesYear     = currentYear   === 'all' || year === currentYear;

            if (matchesCategory && matchesYear) {
                item.style.display = 'block';
                item.classList.add('fade-in');
            } else {
                item.style.display = 'none';
                item.classList.remove('fade-in');
            }
        });
    }

    filterButtons.forEach(button => {
        button.addEventListener('click', function () {
            filterButtons.forEach(btn => btn.classList.remove('active'));
            this.classList.add('active');
            currentFilter = this.getAttribute('data-filter');
            filterGallery();
        });
    });

    yearButtons.forEach(button => {
        button.addEventListener('click', function () {
            yearButtons.forEach(btn => btn.classList.remove('active'));
            this.classList.add('active');
            currentYear = this.getAttribute('data-year');
            filterGallery();
        });
    });

    // Load More — only attach if the button exists
    if (loadMoreBtn) {
        loadMoreBtn.addEventListener('click', function () {
            currentPage++;

            loadMoreBtn.innerHTML = '<span class="spinner-border spinner-border-sm me-2"></span>Loading...';
            loadMoreBtn.disabled  = true;

            fetch(`/gallery/load?page=${currentPage}&size=${pageSize}`)
                .then(res => res.json())
                .then(data => {
                    if (data.length === 0) {
                        loadMoreBtn.style.display = 'none';
                        return;
                    }

                    data.forEach(artwork => {
                        const col = document.createElement('div');
                        col.className = 'col-lg-4 col-md-6 gallery-item-wrapper fade-in';
                        col.setAttribute('data-category', artwork.medium || '');
                        col.setAttribute('data-year', (artwork.createdDate && artwork.createdDate.year) ? artwork.createdDate.year : '');

                        col.innerHTML = `
                            <div class="art-card">
                                <div class="art-card-img-container">
                                    <span class="art-card-badge">${artwork.medium || ''}</span>
                                    <img src="/images/${artwork.imgUrl}" alt="${artwork.title}">
                                </div>
                                <div class="art-overlay-info">
                                    <h4>${artwork.title}</h4>
                                    <p class="small text-muted">
                                        ${artwork.size || ''} • ${artwork.yearCreated || ''}
                                    </p>
                                    <strong>₹${artwork.price}</strong>
                                </div>
                            </div>
                        `;
                        galleryGrid.appendChild(col);
                    });

                    loadMoreBtn.innerHTML = '<i class="bi bi-plus-lg me-2"></i> Load More Artworks';
                    loadMoreBtn.disabled  = false;
                })
                .catch(() => {
                    loadMoreBtn.innerHTML = 'Error Loading';
                    loadMoreBtn.disabled  = false;
                });
        });
    }

    // Initial filter run
    filterGallery();
});


// =====================================================================
// SECTION 3: Shop Page — Cart, Wishlist, Filters, Sorting
// =====================================================================
document.addEventListener('DOMContentLoaded', function () {

    // Only run shop logic if the shop page cart element exists
    const cartCountEl = document.querySelector('.cart-count');
    if (!cartCountEl) return;

    let cart     = JSON.parse(localStorage.getItem('artCart'))    || {};
    let wishlist = JSON.parse(localStorage.getItem('artWishlist')) || [];

    // -- Update UI Functions --

    function updateCartCount() {
        let totalItems = 0;
        for (let item in cart) totalItems += cart[item].quantity;
        cartCountEl.textContent = totalItems;
    }

    function updateWishlistButtons() {
        document.querySelectorAll('.wishlist-btn').forEach(btn => {
            const itemId    = btn.getAttribute('data-item');
            const heartIcon = btn.querySelector('i');
            if (!heartIcon) return;

            if (wishlist.includes(itemId)) {
                btn.classList.add('active');
                heartIcon.classList.remove('bi-heart');
                heartIcon.classList.add('bi-heart-fill');
            } else {
                btn.classList.remove('active');
                heartIcon.classList.remove('bi-heart-fill');
                heartIcon.classList.add('bi-heart');
            }
        });
    }

    function updateCartModal() {
        const container = document.getElementById('cartItems');
        if (!container) return;

        let subtotal = 0;

        if (Object.keys(cart).length === 0) {
            container.innerHTML = '<p class="text-center text-muted py-4">Your cart is empty.</p>';
        } else {
            container.innerHTML = '';
            for (let itemId in cart) {
                const item      = cart[itemId];
                const itemTotal = item.price * item.quantity;
                subtotal       += itemTotal;

                container.innerHTML += `
                    <div class="d-flex align-items-center mb-3 pb-3 border-bottom">
                        <img src="${item.image}" style="width: 60px; height: 60px; object-fit: cover; border-radius: 8px;" class="me-3">
                        <div style="flex: 1;">
                            <h6 class="mb-0">${item.name}</h6>
                            <small class="text-muted">${item.type}</small>
                            <div class="d-flex align-items-center mt-2">
                                <button class="btn btn-sm btn-light border rounded-circle p-0" style="width:24px;height:24px;" onclick="changeCartQty('${itemId}', -1)">-</button>
                                <span class="mx-2 small">${item.quantity}</span>
                                <button class="btn btn-sm btn-light border rounded-circle p-0" style="width:24px;height:24px;" onclick="changeCartQty('${itemId}', 1)">+</button>
                            </div>
                        </div>
                        <div class="text-end">
                            <div class="fw-bold">₹${itemTotal}</div>
                            <button class="btn btn-link text-danger p-0 small text-decoration-none" onclick="removeFromCart('${itemId}')">Remove</button>
                        </div>
                    </div>
                `;
            }
        }

        const subtotalEl = document.getElementById('cartSubtotal');
        const totalEl    = document.getElementById('cartTotal');
        if (subtotalEl) subtotalEl.textContent = `₹${subtotal}`;
        if (totalEl)    totalEl.textContent    = `₹${subtotal}`;
    }

    // -- Add to Cart --
    document.querySelectorAll('.add-to-cart').forEach(btn => {
        btn.addEventListener('click', function () {
            const id      = this.getAttribute('data-item');
            const price   = parseFloat(this.getAttribute('data-price'));
            const qtySpan = document.querySelector(`.quantity[data-item="${id}"]`);
            const quantity = qtySpan ? parseInt(qtySpan.textContent) : 1;
            const card    = this.closest('.shop-item');
            const name    = card ? (card.querySelector('h4') ? card.querySelector('h4').textContent : '') : '';
            const image   = card ? (card.querySelector('img') ? card.querySelector('img').src : '') : '';
            const type    = card ? (card.querySelector('.art-card-badge') ? card.querySelector('.art-card-badge').textContent : '') : '';

            if (cart[id]) {
                cart[id].quantity += quantity;
            } else {
                cart[id] = { name, price, quantity, image, type };
            }

            localStorage.setItem('artCart', JSON.stringify(cart));
            updateCartCount();
            showNotification(`Added ${quantity} ${name} to cart`);
            if (qtySpan) qtySpan.textContent = 1;
        });
    });

    // -- Page Quantity Selectors --
    document.querySelectorAll('.quantity-btn').forEach(btn => {
        btn.addEventListener('click', function () {
            const id   = this.getAttribute('data-item');
            const span = document.querySelector(`.quantity[data-item="${id}"]`);
            if (!span) return;
            let val = parseInt(span.textContent);
            if (this.classList.contains('plus'))      val++;
            else if (val > 1)                          val--;
            span.textContent = val;
        });
    });

    // -- Wishlist --
    document.querySelectorAll('.wishlist-btn').forEach(btn => {
        btn.addEventListener('click', function () {
            const id = this.getAttribute('data-item');
            if (wishlist.includes(id)) {
                wishlist = wishlist.filter(x => x !== id);
                showNotification('Removed from wishlist');
            } else {
                wishlist.push(id);
                showNotification('Added to wishlist');
            }
            localStorage.setItem('artWishlist', JSON.stringify(wishlist));
            updateWishlistButtons();
        });
    });

    // -- Shop Filter --
    const shopFilterBtns = document.querySelectorAll('.filter-btn');
    const shopItems      = document.querySelectorAll('.shop-item-container');

    shopFilterBtns.forEach(btn => {
        btn.addEventListener('click', function () {
            shopFilterBtns.forEach(b => b.classList.remove('active'));
            this.classList.add('active');

            const filter = this.dataset.filter.toLowerCase();

            shopItems.forEach(item => {
                const category = item.dataset.category;
                const status   = item.dataset.status;
                let show       = true;

                if      (filter === 'originals') show = category === 'originals';
                else if (filter === 'prints')    show = category === 'prints';
                else if (filter === 'instock')   show = status === 'Available' || status === 'Reserved';

                item.style.display = show ? 'block' : 'none';
                if (show) item.classList.add('fade-in');
            });
        });
    });

    // -- Sorting --
    const sortSelect = document.getElementById('sortSelect');
    if (sortSelect) {
        sortSelect.addEventListener('change', function () {
            const sortBy    = this.value;
            const container = document.getElementById('shopItems');
            if (!container) return;

            const items = Array.from(container.querySelectorAll('.shop-item-container'));
            items.sort((a, b) => {
                const pA = parseFloat(a.getAttribute('data-price'));
                const pB = parseFloat(b.getAttribute('data-price'));
                const dA = parseInt(a.getAttribute('data-date'));
                const dB = parseInt(b.getAttribute('data-date'));

                if (sortBy === 'price-low')  return pA - pB;
                if (sortBy === 'price-high') return pB - pA;
                if (sortBy === 'newest')     return dB - dA;
                return 0;
            });

            items.forEach(item => container.appendChild(item));
        });
    }

    // -- Cart Modal Global Functions --
    window.changeCartQty = function (id, change) {
        if (cart[id]) {
            cart[id].quantity += change;
            if (cart[id].quantity < 1) delete cart[id];
            localStorage.setItem('artCart', JSON.stringify(cart));
            updateCartCount();
            updateCartModal();
        }
    };

    window.removeFromCart = function (id) {
        delete cart[id];
        localStorage.setItem('artCart', JSON.stringify(cart));
        updateCartCount();
        updateCartModal();
    };

    // -- Cart Modal Open/Close --
    const cartSummaryEl = document.getElementById('cartSummary');
    const cartModalEl   = document.getElementById('cartModal');
    const cartOverlayEl = document.getElementById('cartOverlay');
    const closeCartEl   = document.getElementById('closeCart');
    const checkoutBtnEl = document.getElementById('checkoutBtn');

    if (cartSummaryEl && cartModalEl && cartOverlayEl) {
        cartSummaryEl.onclick = () => {
            updateCartModal();
            cartModalEl.style.display   = 'block';
            cartOverlayEl.style.display = 'block';
        };

        const closeCart = () => {
            cartModalEl.style.display   = 'none';
            cartOverlayEl.style.display = 'none';
        };

        if (closeCartEl)   closeCartEl.onclick   = closeCart;
        if (cartOverlayEl) cartOverlayEl.onclick  = closeCart;
    }

    if (checkoutBtnEl) {
        checkoutBtnEl.onclick = () => {
            const currentCart = JSON.parse(localStorage.getItem('artCart')) || {};
            if (Object.keys(currentCart).length === 0) {
                alert('Your cart is empty');
                return;
            }
            // Navigate to checkout
            window.location.href = '/checkout';
        };
    }

    // -- Notification Helper --
    function showNotification(msg) {
        const n = document.createElement('div');
        n.textContent = msg;
        n.style.cssText = `
            position: fixed; top: 100px; right: 20px;
            background: var(--dark-color, #222); color: white;
            padding: 10px 20px; border-radius: 8px;
            z-index: 9999; animation: fadeInUp 0.3s;
        `;
        document.body.appendChild(n);
        setTimeout(() => n.remove(), 3000);
    }

    // -- Init --
    updateCartCount();
    updateWishlistButtons();
});


// =====================================================================
// SECTION 4: Exhibition Countdown Timer
// =====================================================================
document.addEventListener('DOMContentLoaded', function () {
    const cards = document.querySelectorAll('.exhibition-card');
    if (cards.length === 0) return; // not on a page with exhibitions

    function updateCountdown() {
        const now = new Date().getTime();
        cards.forEach(card => {
            const startDateStr = card.dataset.start;
            if (!startDateStr) return;

            const startDate = new Date(startDateStr.replace(' ', 'T')).getTime();
            const diff      = startDate - now;
            const countdownBox = card.querySelector('.countdown-box');
            if (!countdownBox) return;

            if (diff <= 0) {
                countdownBox.innerHTML = "<span class='text-success fw-bold'>Started</span>";
                return;
            }

            const days    = Math.floor(diff / (1000 * 60 * 60 * 24));
            const hours   = Math.floor((diff / (1000 * 60 * 60)) % 24);
            const minutes = Math.floor((diff / (1000 * 60)) % 60);

            const d1 = card.querySelector('.countdown-box div:nth-child(1) .count-num');
            const d2 = card.querySelector('.countdown-box div:nth-child(2) .count-num');
            const d3 = card.querySelector('.countdown-box div:nth-child(3) .count-num');

            if (d1) d1.textContent = days;
            if (d2) d2.textContent = hours;
            if (d3) d3.textContent = minutes;
        });
    }

    updateCountdown();
    setInterval(updateCountdown, 60000);
});


// =====================================================================
// SECTION 5: Contact Form — Submit Feedback
// =====================================================================
document.addEventListener('DOMContentLoaded', function () {
    const form = document.getElementById('contactForm');
    if (!form) return; // not on the contact page

    form.addEventListener('submit', function (e) {
        const btn          = form.querySelector('button[type="submit"]');
        if (!btn) return;

        const originalText = btn.innerHTML;

        btn.innerHTML = '<span class="spinner-border spinner-border-sm me-2"></span>Sending...';
        btn.disabled  = true;

        setTimeout(() => {
            btn.innerHTML = '<i class="bi bi-check-circle me-2"></i>Message Sent!';
            btn.classList.remove('btn-primary');
            btn.classList.add('btn-success');
            form.reset();

            setTimeout(() => {
                btn.innerHTML = originalText;
                btn.classList.remove('btn-success');
                btn.classList.add('btn-primary');
                btn.disabled = false;
            }, 3000);
        }, 1500);
    });
});


// =====================================================================
// SECTION 6: Admin Panel — Sidebar Toggle + Tab Logic
// =====================================================================
(function () {
    const toggleBtn = document.getElementById('sidebarToggle');
    const sidebar   = document.getElementById('sidebar');

    // Only run admin logic if sidebar exists (i.e., we are on an admin page)
    if (!toggleBtn || !sidebar) return;

    toggleBtn.addEventListener('click', () => {
        sidebar.classList.toggle('active');
    });

    document.addEventListener('click', (e) => {
        if (window.innerWidth < 992) {
            if (!sidebar.contains(e.target) && !toggleBtn.contains(e.target)) {
                sidebar.classList.remove('active');
            }
        }
    });
})();


// =====================================================================
// SECTION 7: Admin — Generic openEdit() for all modals
// =====================================================================
function openEdit(btn) {
    const id           = btn.dataset.id;
    const fetchUrl     = `${btn.dataset.fetch}/${id}`;
    const updateUrl    = btn.dataset.update;
    const modalSelector = btn.dataset.modal;
    const modalTitle   = btn.dataset.title;

    if (!fetchUrl || !modalSelector) return;

    fetch(fetchUrl)
        .then(res => res.json())
        .then(data => {
            const modal = document.querySelector(modalSelector);
            if (!modal) return;

            const form = modal.querySelector('form');
            if (!form) return;

            // Set form action to the update URL
            form.action = updateUrl;

            // Set modal title
            const titleEl = modal.querySelector('.modal-title');
            if (titleEl) titleEl.innerText = modalTitle;

            // Populate form inputs automatically using data keys
            Object.keys(data).forEach(key => {
                const field = form.querySelector(`[name="${key}"]`);
                if (!field) return;

                if (field.type === 'radio') {
                    const radioField = form.querySelector(`[name="${key}"][value="${data[key]}"]`);
                    if (radioField) radioField.checked = true;
                } else {
                    field.value = data[key] !== null && data[key] !== undefined ? data[key] : '';
                }
            });

            // Ensure hidden ID input exists and is set
            let idInput = form.querySelector('input[name="id"]');
            if (!idInput) {
                idInput      = document.createElement('input');
                idInput.type = 'hidden';
                idInput.name = 'id';
                form.appendChild(idInput);
            }
            idInput.value = data.id;

            new bootstrap.Modal(modal).show();
        })
        .catch(err => console.error('openEdit fetch error:', err));
}


// =====================================================================
// SECTION 8: Admin — Generic openAdd() for all modals
// =====================================================================
function openAdd(btn) {
    const modal = document.querySelector(btn.dataset.modal);
    if (!modal) return;

    const form = modal.querySelector('form');
    if (!form) return;

    // Reset form and set action to add URL
    form.reset();
    form.action = btn.dataset.add;

    // Clear any hidden ID fields
    const entityIdField = modal.querySelector('#entityId');
    if (entityIdField) {
        entityIdField.value = '';
    } else {
        const idField = modal.querySelector("input[name='id']");
        if (idField) idField.value = '';
    }

    // Set modal title
    const titleEl = modal.querySelector('.modal-title');
    if (titleEl) titleEl.innerText = btn.dataset.title;

    new bootstrap.Modal(modal).show();
}


// =====================================================================
// SECTION 9: Admin — Reset Modal State on Close
// =====================================================================
document.querySelectorAll('.modal').forEach(modal => {
    modal.addEventListener('hidden.bs.modal', () => {
        const form = modal.querySelector('form');
        if (!form) return;

        form.reset();
        form.querySelectorAll('input[name="id"]').forEach(i => i.remove());
    });
});
