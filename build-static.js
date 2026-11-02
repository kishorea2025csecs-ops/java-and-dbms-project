const fs = require('fs');
const path = require('path');

const publicDir = path.join(__dirname, 'public');
if (!fs.existsSync(publicDir)) fs.mkdirSync(publicDir, { recursive: true });

function getHead(title) {
    return `
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${title} - ParkEase Smart Parking System</title>
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@300;400;500;600;700;800&display=swap" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.2/css/all.min.css" rel="stylesheet">
    <link rel="stylesheet" href="/css/style.css">
`;
}

function getSidebar(activeNav) {
    const navs = [
        { section: 'Operations' },
        { id: 'dashboard', href: '/dashboard.html', icon: 'fa-chart-pie', label: 'Dashboard' },
        { id: 'park-vehicle', href: '/park-vehicle.html', icon: 'fa-car-side', label: 'Park Vehicle' },
        { id: 'vehicle-exit', href: '/vehicle-exit.html', icon: 'fa-door-open', label: 'Vehicle Exit' },
        { id: 'parking-slots', href: '/parking-slots.html', icon: 'fa-th-large', label: 'Live Parking Slots' },
        { section: 'Records & Management' },
        { id: 'tickets', href: '/tickets.html', icon: 'fa-ticket-alt', label: 'Parking Tickets' },
        { id: 'vehicles', href: '/vehicles.html', icon: 'fa-list-alt', label: 'Vehicle Management' },
        { id: 'billing', href: '/billing.html', icon: 'fa-credit-card', label: 'Billing & Payments' },
        { id: 'reports', href: '/reports.html', icon: 'fa-chart-line', label: 'Reports & Analytics' },
        { section: 'System & Database' },
        { id: 'database-records', href: '/database-records.html', icon: 'fa-database', label: 'Database Records' },
        { id: 'settings', href: '/settings.html', icon: 'fa-sliders-h', label: 'Settings' }
    ];

    let itemsHtml = '';
    for (const item of navs) {
        if (item.section) {
            itemsHtml += `<li class="nav-section-title">${item.section}</li>\n`;
        } else {
            const isActive = item.id === activeNav ? ' active' : '';
            itemsHtml += `
            <li class="nav-item">
                <a href="${item.href}" class="nav-link${isActive}">
                    <i class="fas ${item.icon}"></i>
                    <span>${item.label}</span>
                </a>
            </li>\n`;
        }
    }

    return `
    <aside class="sidebar">
        <div class="sidebar-brand">
            <div class="brand-icon">
                <i class="fas fa-parking"></i>
            </div>
            <div class="brand-text">
                <h1>ParkEase</h1>
                <span>Smart Parking System</span>
            </div>
        </div>
        <ul class="sidebar-nav">
            ${itemsHtml}
            <li class="nav-item mt-3">
                <a href="/login.html" class="nav-link text-danger">
                    <i class="fas fa-sign-out-alt text-danger"></i>
                    <span>Logout</span>
                </a>
            </li>
        </ul>
    </aside>
`;
}

function getTopbar(title, subtitle) {
    return `
    <header class="topbar">
        <div class="topbar-left">
            <button class="btn btn-sm btn-outline-secondary d-md-none me-2" id="sidebarToggle">
                <i class="fas fa-bars"></i>
            </button>
            <div class="topbar-title">
                <h2>${title}</h2>
                <p>${subtitle}</p>
            </div>
        </div>
        <div class="topbar-right">
            <div class="d-none d-sm-flex align-items-center gap-2 border px-3 py-1 rounded-pill bg-light text-muted small">
                <span class="badge bg-success rounded-circle p-1"></span>
                <span>Deploy: <strong class="text-dark">Netlify Live</strong></span>
            </div>
            <div class="d-flex align-items-center gap-2">
                <a href="/park-vehicle.html" class="btn btn-sm btn-teal shadow-sm">
                    <i class="fas fa-plus-circle me-1"></i> Park Vehicle
                </a>
                <a href="/vehicle-exit.html" class="btn btn-sm btn-outline-danger shadow-sm">
                    <i class="fas fa-sign-out-alt me-1"></i> Exit
                </a>
            </div>
        </div>
    </header>
`;
}

function getScripts(extraScript = '') {
    return `
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/chart.js@4.4.1/dist/chart.umd.min.js"></script>
    <script src="/js/parkease-engine.js"></script>
    <script src="/js/app.js"></script>
    <script>
        const toggleBtn = document.getElementById('sidebarToggle');
        if (toggleBtn) {
            toggleBtn.addEventListener('click', () => {
                document.querySelector('.sidebar').classList.toggle('d-none');
            });
        }
    </script>
    ${extraScript}
`;
}

// ------------------------------------------------------------------------------
// Page 1: index.html
// ------------------------------------------------------------------------------
fs.writeFileSync(path.join(publicDir, 'index.html'), `<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="refresh" content="0; url=/dashboard.html">
    <title>Redirecting to ParkEase Dashboard...</title>
</head>
<body>
    <script>window.location.href = '/dashboard.html';</script>
</body>
</html>`);

// ------------------------------------------------------------------------------
// Page 2: login.html
// ------------------------------------------------------------------------------
fs.writeFileSync(path.join(publicDir, 'login.html'), `<!DOCTYPE html>
<html lang="en">
<head>
    ${getHead('Admin Login')}
</head>
<body class="bg-light d-flex align-items-center justify-content-center min-vh-100 p-3">
<div class="card border-0 shadow-lg" style="max-width: 440px; width: 100%; border-radius: 18px; overflow: hidden;">
    <div class="p-4 text-white text-center" style="background-color: var(--primary-navy);">
        <div class="brand-icon mx-auto mb-3" style="width: 56px; height: 56px; font-size: 26px;">
            <i class="fas fa-parking"></i>
        </div>
        <h3 class="fw-bold mb-1">ParkEase</h3>
        <p class="text-secondary small mb-0">Smart Parking. Easy Management.</p>
    </div>

    <div class="card-body p-4 p-md-5">
        <h5 class="fw-bold mb-2 text-dark">Administrator Sign In</h5>
        <p class="text-muted small mb-4">Enter your administrative credentials to manage parking slots, tickets, and transactions.</p>

        <form id="loginForm" onsubmit="event.preventDefault(); window.location.href='/dashboard.html';">
            <div class="mb-3">
                <label class="form-label small fw-semibold text-muted">Username</label>
                <div class="input-group">
                    <span class="input-group-text bg-white"><i class="fas fa-user text-muted"></i></span>
                    <input type="text" id="loginUsername" class="form-control" placeholder="admin" required value="admin">
                </div>
            </div>

            <div class="mb-4">
                <label class="form-label small fw-semibold text-muted">Password</label>
                <div class="input-group">
                    <span class="input-group-text bg-white"><i class="fas fa-lock text-muted"></i></span>
                    <input type="password" id="loginPassword" class="form-control" placeholder="admin123" required value="admin123">
                </div>
            </div>

            <button type="submit" class="btn btn-teal w-100 py-2 fw-semibold shadow-sm mb-3">
                <i class="fas fa-sign-in-alt me-2"></i> Sign In to Dashboard
            </button>
        </form>

        <div class="p-3 bg-light rounded-3 text-center border">
            <div class="small text-muted fw-semibold mb-1">Demo Credentials:</div>
            <div class="small text-dark mb-2">Username: <strong>admin</strong> &bull; Password: <strong>admin123</strong></div>
            <button type="button" class="btn btn-sm btn-outline-secondary w-100" onclick="window.location.href='/dashboard.html'">
                <i class="fas fa-bolt text-warning me-1"></i> Auto Sign In
            </button>
        </div>
    </div>
</div>
</body>
</html>`);

// ------------------------------------------------------------------------------
// Page 3: dashboard.html
// ------------------------------------------------------------------------------
fs.writeFileSync(path.join(publicDir, 'dashboard.html'), `<!DOCTYPE html>
<html lang="en">
<head>
    ${getHead('Dashboard')}
</head>
<body>
<div class="app-wrapper">
    ${getSidebar('dashboard')}
    <div class="main-wrapper">
        ${getTopbar('Dashboard Overview', 'Real-time parking analytics &amp; operational metrics')}
        <main class="content-body">
            <!-- 6 Key Statistics Cards -->
            <div class="row g-3 mb-4">
                <div class="col-xl-2 col-md-4 col-sm-6">
                    <div class="stat-card">
                        <div class="stat-content">
                            <div class="stat-label">Total Slots</div>
                            <div class="stat-value" id="statTotalSlots">20</div>
                        </div>
                        <div class="stat-icon primary"><i class="fas fa-th"></i></div>
                    </div>
                </div>
                <div class="col-xl-2 col-md-4 col-sm-6">
                    <div class="stat-card">
                        <div class="stat-content">
                            <div class="stat-label">Occupied</div>
                            <div class="stat-value text-danger" id="statOccupiedSlots">2</div>
                        </div>
                        <div class="stat-icon danger"><i class="fas fa-car"></i></div>
                    </div>
                </div>
                <div class="col-xl-2 col-md-4 col-sm-6">
                    <div class="stat-card">
                        <div class="stat-content">
                            <div class="stat-label">Available</div>
                            <div class="stat-value text-success" id="statAvailableSlots">18</div>
                        </div>
                        <div class="stat-icon success"><i class="fas fa-check-circle"></i></div>
                    </div>
                </div>
                <div class="col-xl-2 col-md-4 col-sm-6">
                    <div class="stat-card">
                        <div class="stat-content">
                            <div class="stat-label">Occupancy Rate</div>
                            <div class="stat-value text-primary" id="statOccupancyRate">10%</div>
                        </div>
                        <div class="stat-icon teal"><i class="fas fa-percentage"></i></div>
                    </div>
                </div>
                <div class="col-xl-2 col-md-4 col-sm-6">
                    <div class="stat-card">
                        <div class="stat-content">
                            <div class="stat-label">Total Revenue</div>
                            <div class="stat-value text-dark" id="statRevenue">₹120</div>
                        </div>
                        <div class="stat-icon warning"><i class="fas fa-rupee-sign"></i></div>
                    </div>
                </div>
                <div class="col-xl-2 col-md-4 col-sm-6">
                    <div class="stat-card">
                        <div class="stat-content">
                            <div class="stat-label">Active Tickets</div>
                            <div class="stat-value text-teal" id="statActiveTickets">2</div>
                        </div>
                        <div class="stat-icon purple"><i class="fas fa-ticket-alt"></i></div>
                    </div>
                </div>
            </div>

            <!-- Charts & Live Overview -->
            <div class="row g-4 mb-4">
                <div class="col-lg-8">
                    <div class="card card-custom p-4 h-100">
                        <div class="d-flex justify-content-between align-items-center mb-3">
                            <div>
                                <h5 class="fw-bold mb-1 text-dark">Daily Revenue Collection</h5>
                                <p class="text-muted small mb-0">Daily income trend from completed parking sessions</p>
                            </div>
                            <span class="badge bg-light text-dark border px-3 py-2">
                                <i class="fas fa-calendar-alt text-teal me-1"></i> Live Analytics
                            </span>
                        </div>
                        <div style="height: 280px; position: relative;">
                            <canvas id="revenueChart"></canvas>
                        </div>
                    </div>
                </div>
                <div class="col-lg-4">
                    <div class="card card-custom p-4 h-100">
                        <h5 class="fw-bold mb-1 text-dark">Vehicle Distribution</h5>
                        <p class="text-muted small mb-3">Active parking distribution by category</p>
                        <div style="height: 240px; position: relative;">
                            <canvas id="vehicleTypeChart"></canvas>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Recent Active Tickets Table -->
            <div class="card card-custom p-4">
                <div class="d-flex justify-content-between align-items-center mb-3">
                    <h5 class="fw-bold mb-0 text-dark">Active Parked Vehicles</h5>
                    <a href="/tickets.html" class="btn btn-sm btn-outline-secondary">View All Tickets &rarr;</a>
                </div>
                <div class="table-responsive">
                    <table class="table table-hover align-middle mb-0">
                        <thead class="table-light">
                            <tr>
                                <th>Ticket ID</th>
                                <th>Vehicle No.</th>
                                <th>Owner</th>
                                <th>Type</th>
                                <th>Bay(s)</th>
                                <th>Entry Time</th>
                                <th>Rate</th>
                                <th>Action</th>
                            </tr>
                        </thead>
                        <tbody id="recentTicketsTable"></tbody>
                    </table>
                </div>
            </div>
        </main>
    </div>
</div>
${getScripts(`
<script>
    function renderDashboard() {
        const stats = ParkEaseDB.getStats();
        const db = ParkEaseDB.load();

        document.getElementById('statTotalSlots').innerText = stats.totalSlots;
        document.getElementById('statOccupiedSlots').innerText = stats.occupiedSlots;
        document.getElementById('statAvailableSlots').innerText = stats.availableSlots;
        document.getElementById('statOccupancyRate').innerText = stats.occupancyRate + '%';
        document.getElementById('statRevenue').innerText = '₹' + stats.totalRevenue;
        document.getElementById('statActiveTickets').innerText = stats.activeTickets;

        // Render Recent Active Tickets
        const tbody = document.getElementById('recentTicketsTable');
        const activeTickets = db.tickets.filter(t => t.status === 'ACTIVE');
        if (activeTickets.length === 0) {
            tbody.innerHTML = '<tr><td colspan="8" class="text-center text-muted py-3">No active parked vehicles right now.</td></tr>';
        } else {
            tbody.innerHTML = activeTickets.map(t => \`
                <tr>
                    <td><span class="badge bg-light text-dark border font-monospace">\${t.ticketNumber}</span></td>
                    <td><strong class="font-monospace">\${t.vehicleNumber}</strong></td>
                    <td>\${t.ownerName}</td>
                    <td><span class="badge bg-info text-dark">\${t.vehicleType}</span></td>
                    <td><span class="badge bg-teal text-white">\${t.allocatedSlots}</span></td>
                    <td class="small text-muted">\${new Date(t.entryTime).toLocaleTimeString([], {hour: '2-digit', minute:'2-digit'})}</td>
                    <td>₹\${t.hourlyRate}/hr</td>
                    <td>
                        <a href="/vehicle-exit.html?ticket=\${t.ticketNumber}" class="btn btn-sm btn-outline-danger py-0 px-2">Exit</a>
                    </td>
                </tr>
            \`).join('');
        }

        // Charts
        const revCtx = document.getElementById('revenueChart');
        if (revCtx) {
            new Chart(revCtx, {
                type: 'bar',
                data: {
                    labels: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Today'],
                    datasets: [{
                        label: 'Revenue (₹)',
                        data: [150, 240, 180, 310, 290, 420, stats.totalRevenue],
                        backgroundColor: '#0d9488',
                        borderRadius: 6
                    }]
                },
                options: { responsive: true, maintainAspectRatio: false }
            });
        }

        const vehCtx = document.getElementById('vehicleTypeChart');
        if (vehCtx) {
            const cars = db.tickets.filter(t => t.vehicleType === 'CAR' && t.status === 'ACTIVE').length;
            const bikes = db.tickets.filter(t => t.vehicleType === 'BIKE' && t.status === 'ACTIVE').length;
            const trucks = db.tickets.filter(t => t.vehicleType === 'TRUCK' && t.status === 'ACTIVE').length;
            new Chart(vehCtx, {
                type: 'doughnut',
                data: {
                    labels: ['Cars', 'Bikes', 'Trucks'],
                    datasets: [{
                        data: [cars || 1, bikes || 1, trucks || 0],
                        backgroundColor: ['#2563eb', '#0d9488', '#f59e0b']
                    }]
                },
                options: { responsive: true, maintainAspectRatio: false }
            });
        }
    }
    document.addEventListener('DOMContentLoaded', renderDashboard);
</script>
`)}
</body>
</html>`);

// ------------------------------------------------------------------------------
// Page 4: parking-slots.html
// ------------------------------------------------------------------------------
fs.writeFileSync(path.join(publicDir, 'parking-slots.html'), `<!DOCTYPE html>
<html lang="en">
<head>
    ${getHead('Live Parking Slots')}
</head>
<body>
<div class="app-wrapper">
    ${getSidebar('parking-slots')}
    <div class="main-wrapper">
        ${getTopbar('Live Parking Slots', 'Real-time 20-bay layout tracking occupancy and allocation')}
        <main class="content-body">
            <div class="card card-custom p-3 mb-4">
                <div class="d-flex flex-wrap justify-content-between align-items-center gap-3">
                    <div class="btn-group" role="group">
                        <button type="button" class="btn btn-outline-secondary btn-sm active" onclick="filterSlots('all')">All Bays (20)</button>
                        <button type="button" class="btn btn-outline-success btn-sm" onclick="filterSlots('available')">Available</button>
                        <button type="button" class="btn btn-outline-danger btn-sm" onclick="filterSlots('occupied')">Occupied</button>
                    </div>
                    <div class="d-flex align-items-center gap-4 small">
                        <div class="d-flex align-items-center gap-2">
                            <span class="badge bg-success rounded-circle p-1" style="width: 12px; height: 12px;"></span>
                            <span class="text-muted">Green: Available</span>
                        </div>
                        <div class="d-flex align-items-center gap-2">
                            <span class="badge bg-danger rounded-circle p-1" style="width: 12px; height: 12px;"></span>
                            <span class="text-muted">Red: Occupied</span>
                        </div>
                        <a href="/park-vehicle.html" class="btn btn-sm btn-teal ms-2">
                            <i class="fas fa-plus me-1"></i> Park Here
                        </a>
                    </div>
                </div>
            </div>

            <!-- Exactly 20 Parking Slots Grid -->
            <div class="slots-grid" id="slotsContainer"></div>
        </main>
    </div>
</div>
${getScripts(`
<script>
    function renderSlots(filter = 'all') {
        const db = ParkEaseDB.load();
        const container = document.getElementById('slotsContainer');
        const slots = db.slots;

        container.innerHTML = slots.map(slot => {
            const isOcc = slot.status === 'OCCUPIED';
            if (filter === 'available' && isOcc) return '';
            if (filter === 'occupied' && !isOcc) return '';

            return \`
                <div class="slot-bay \${slot.status.toLowerCase()}" data-status="\${slot.status.toLowerCase()}">
                    <div class="slot-header">
                        <span class="slot-number">\${slot.slotNumber}</span>
                        <span class="slot-status-pill \${slot.status.toLowerCase()}">\${slot.status}</span>
                    </div>
                    <div class="slot-body">
                        \${isOcc ? \`
                            <div class="slot-veh-num font-monospace">\${slot.vehicleNumber}</div>
                            <div class="slot-veh-owner"><i class="fas fa-user-circle me-1"></i>\${slot.ownerName}</div>
                            <div class="small mt-1"><span class="badge bg-light text-dark border">\${slot.vehicleType}</span></div>
                        \` : \`
                            <div class="text-center py-2 text-success opacity-75">
                                <i class="fas fa-parking fa-2x mb-1"></i>
                                <div class="small fw-semibold">Ready for Parking</div>
                            </div>
                        \`}
                    </div>
                    <div class="slot-footer pt-2 border-top">
                        \${isOcc ? \`
                            <a href="/vehicle-exit.html?ticket=\${slot.ticketNumber}" class="btn btn-xs btn-outline-danger w-100">
                                <i class="fas fa-sign-out-alt me-1"></i> Process Exit
                            </a>
                        \` : \`
                            <a href="/park-vehicle.html" class="btn btn-xs btn-outline-success w-100">
                                <i class="fas fa-car me-1"></i> Allocate Bay
                            </a>
                        \`}
                    </div>
                </div>
            \`;
        }).join('');
    }

    function filterSlots(filter) {
        document.querySelectorAll('.btn-group button').forEach(b => b.classList.remove('active'));
        event.target.classList.add('active');
        renderSlots(filter);
    }

    document.addEventListener('DOMContentLoaded', () => renderSlots('all'));
</script>
`)}
</body>
</html>`);

// ------------------------------------------------------------------------------
// Page 5: park-vehicle.html
// ------------------------------------------------------------------------------
fs.writeFileSync(path.join(publicDir, 'park-vehicle.html'), `<!DOCTYPE html>
<html lang="en">
<head>
    ${getHead('Park Vehicle')}
</head>
<body>
<div class="app-wrapper">
    ${getSidebar('park-vehicle')}
    <div class="main-wrapper">
        ${getTopbar('Park Vehicle', 'Register vehicle, assign parking bay, and issue parking ticket')}
        <main class="content-body">
            <div class="row justify-content-center">
                <div class="col-lg-8">
                    <div class="card card-custom p-4 p-md-5 mb-4">
                        <div class="d-flex align-items-center gap-3 mb-4 pb-3 border-bottom">
                            <div class="stat-icon teal"><i class="fas fa-car-side"></i></div>
                            <div>
                                <h4 class="fw-bold mb-1 text-dark">Vehicle Entry Registration</h4>
                                <p class="text-muted small mb-0">Fill in vehicle details to automatically allocate parking slot(s).</p>
                            </div>
                        </div>

                        <form id="parkVehicleForm">
                            <div class="row g-3">
                                <div class="col-md-6">
                                    <label class="form-label fw-semibold small text-muted">Vehicle Owner Name <span class="text-danger">*</span></label>
                                    <div class="input-group">
                                        <span class="input-group-text bg-white"><i class="fas fa-user text-muted"></i></span>
                                        <input type="text" id="ownerName" class="form-control" placeholder="e.g. Ramesh Kumar" required>
                                    </div>
                                </div>
                                <div class="col-md-6">
                                    <label class="form-label fw-semibold small text-muted">Vehicle Registration Number <span class="text-danger">*</span></label>
                                    <div class="input-group">
                                        <span class="input-group-text bg-white"><i class="fas fa-id-card text-muted"></i></span>
                                        <input type="text" id="vehicleNumber" class="form-control font-monospace text-uppercase" placeholder="e.g. MH-12-AB-1234" required>
                                    </div>
                                </div>
                                <div class="col-md-6">
                                    <label class="form-label fw-semibold small text-muted">Vehicle Category <span class="text-danger">*</span></label>
                                    <div class="input-group">
                                        <span class="input-group-text bg-white"><i class="fas fa-truck-pickup text-muted"></i></span>
                                        <select id="vehicleType" class="form-select" required>
                                            <option value="" disabled selected>-- Select Vehicle Category --</option>
                                            <option value="BIKE">Bike / Two-Wheeler (₹10/hr - 1 Slot)</option>
                                            <option value="CAR">Car / Four-Wheeler (₹30/hr - 1 Slot)</option>
                                            <option value="TRUCK">Truck / Heavy Vehicle (₹60/hr - 2 Consecutive Slots)</option>
                                        </select>
                                    </div>
                                    <div id="rateHint" class="form-text small mt-1">Select category to inspect parking rules.</div>
                                </div>
                                <div class="col-md-6">
                                    <label class="form-label fw-semibold small text-muted">Owner Contact Number <span class="text-danger">*</span></label>
                                    <div class="input-group">
                                        <span class="input-group-text bg-white"><i class="fas fa-phone text-muted"></i></span>
                                        <input type="tel" id="contactNumber" class="form-control" placeholder="e.g. 9876543210" required>
                                    </div>
                                </div>
                                <div class="col-md-12">
                                    <label class="form-label fw-semibold small text-muted">Entry Timestamp (Leave blank for current time)</label>
                                    <div class="input-group">
                                        <span class="input-group-text bg-white"><i class="fas fa-calendar-alt text-muted"></i></span>
                                        <input type="datetime-local" id="customEntryTime" class="form-control">
                                    </div>
                                </div>
                            </div>

                            <div class="p-3 bg-light rounded-3 border mt-4 small text-muted">
                                <div class="fw-semibold text-dark mb-1"><i class="fas fa-info-circle text-teal me-1"></i> System Allocation Rules:</div>
                                <ul class="mb-0 ps-3">
                                    <li>Bikes &amp; Cars occupy <strong>1 slot</strong>.</li>
                                    <li>Trucks require <strong>2 consecutive slots</strong> (e.g. Bays 4 &amp; 5).</li>
                                    <li>Daily billing capped at a maximum of <strong>8 hours</strong> per session.</li>
                                </ul>
                            </div>

                            <div class="d-flex justify-content-end gap-2 mt-4">
                                <button type="reset" class="btn btn-outline-secondary px-4">Reset</button>
                                <button type="submit" class="btn btn-teal px-4 shadow-sm">
                                    <i class="fas fa-check-circle me-1"></i> Allocate Bay &amp; Issue Ticket
                                </button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>

            <!-- Modal for Printable Ticket -->
            <div class="modal fade" id="ticketModal" tabindex="-1">
                <div class="modal-dialog modal-dialog-centered">
                    <div class="modal-content border-0 shadow-lg">
                        <div class="modal-header bg-teal text-white">
                            <h5 class="modal-title fw-bold"><i class="fas fa-ticket-alt me-2"></i>Parking Ticket Issued</h5>
                            <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal"></button>
                        </div>
                        <div class="modal-body p-4 text-center">
                            <div class="badge bg-light text-dark border px-3 py-2 fs-5 font-monospace mb-3" id="modalTicketNum">TCK-1001</div>
                            <h4 class="fw-bold text-dark font-monospace mb-1" id="modalVehNum">MH-12-AB-1234</h4>
                            <p class="text-muted small mb-3">Owner: <span id="modalOwner">Rajesh</span> &bull; Type: <span id="modalType">CAR</span></p>
                            
                            <div class="p-3 bg-light rounded-3 border mb-3 text-start small">
                                <div class="d-flex justify-content-between py-1"><span>Allocated Bay:</span><strong class="text-teal" id="modalSlot">SLOT-01</strong></div>
                                <div class="d-flex justify-content-between py-1"><span>Entry Time:</span><span id="modalEntryTime">Now</span></div>
                                <div class="d-flex justify-content-between py-1"><span>Hourly Rate:</span><span id="modalRate">₹30/hr</span></div>
                                <div class="d-flex justify-content-between py-1"><span>Max Daily Cap:</span><span>8 Hours Max</span></div>
                            </div>
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-outline-secondary" data-bs-dismiss="modal">Close</button>
                            <button type="button" class="btn btn-teal" onclick="window.print()"><i class="fas fa-print me-1"></i> Print Ticket</button>
                        </div>
                    </div>
                </div>
            </div>
        </main>
    </div>
</div>
${getScripts()}
</body>
</html>`);

// ------------------------------------------------------------------------------
// Page 6: vehicle-exit.html
// ------------------------------------------------------------------------------
fs.writeFileSync(path.join(publicDir, 'vehicle-exit.html'), `<!DOCTYPE html>
<html lang="en">
<head>
    ${getHead('Vehicle Exit & Payment')}
</head>
<body>
<div class="app-wrapper">
    ${getSidebar('vehicle-exit')}
    <div class="main-wrapper">
        ${getTopbar('Vehicle Exit & Payment', 'Calculate duration, apply 8-hour cap rule, and process payment')}
        <main class="content-body">
            <div class="row justify-content-center">
                <div class="col-lg-8">
                    <!-- Search Card -->
                    <div class="card card-custom p-4 mb-4">
                        <h5 class="fw-bold mb-3 text-dark">Lookup Active Parking Ticket</h5>
                        <div class="input-group">
                            <input type="text" id="exitSearchInput" class="form-control form-control-lg font-monospace text-uppercase" placeholder="Enter Ticket Number (e.g. TCK-1001) or Vehicle Plate">
                            <button class="btn btn-teal px-4" id="btnSearchTicket">
                                <i class="fas fa-search me-2"></i>Search
                            </button>
                        </div>
                    </div>

                    <!-- Exit Calculation Result Card -->
                    <div class="card card-custom p-4 p-md-5 mb-4 d-none" id="exitResultCard">
                        <div class="d-flex justify-content-between align-items-center pb-3 border-bottom mb-4">
                            <div>
                                <h4 class="fw-bold mb-1 text-dark">Exit Fee Calculation</h4>
                                <span class="badge bg-light text-dark border font-monospace" id="calcTicketNumber">TCK-1001</span>
                            </div>
                            <span class="badge bg-warning text-dark px-3 py-2 d-none" id="calcCapBadge">
                                <i class="fas fa-shield-alt me-1"></i> 8-Hour Daily Cap Applied
                            </span>
                        </div>

                        <div class="row g-3 mb-4">
                            <div class="col-sm-6">
                                <label class="text-muted small">Vehicle Number</label>
                                <div class="fw-bold fs-5 font-monospace" id="calcVehNumber">MH-12-AB-1234</div>
                            </div>
                            <div class="col-sm-6">
                                <label class="text-muted small">Owner</label>
                                <div class="fw-bold fs-5" id="calcOwner">Rajesh Kumar</div>
                            </div>
                            <div class="col-sm-6">
                                <label class="text-muted small">Category & Bay</label>
                                <div><span id="calcType" class="badge bg-info text-dark me-2">CAR</span> Bay: <strong id="calcSlot">SLOT-01</strong></div>
                            </div>
                            <div class="col-sm-6">
                                <label class="text-muted small">Duration & Billed Hours</label>
                                <div class="fw-bold text-dark" id="calcDuration">2h 30m (3 billed hrs)</div>
                            </div>
                            <div class="col-sm-6">
                                <label class="text-muted small">Entry Time</label>
                                <div class="small text-muted" id="calcEntryTime"></div>
                            </div>
                            <div class="col-sm-6">
                                <label class="text-muted small">Exit Time</label>
                                <div class="small text-muted" id="calcExitTime"></div>
                            </div>
                        </div>

                        <div class="p-3 rounded-3 bg-light border d-flex justify-content-between align-items-center mb-4">
                            <div>
                                <div class="text-muted small">Rate: <span id="calcRate">₹30/hr</span></div>
                                <div class="fw-bold text-dark fs-5">Total Parking Fee</div>
                            </div>
                            <div class="text-end">
                                <span class="fs-2 fw-extrabold text-teal" id="calcFinalAmount">₹90</span>
                            </div>
                        </div>

                        <!-- Payment Method -->
                        <div class="mb-4">
                            <label class="form-label fw-semibold small text-muted">Select Payment Method</label>
                            <select id="exitPaymentMethod" class="form-select form-select-lg">
                                <option value="CASH">Cash</option>
                                <option value="UPI" selected>UPI (Google Pay / PhonePe / Paytm)</option>
                                <option value="CARD">Debit / Credit Card</option>
                                <option value="NET_BANKING">Net Banking</option>
                            </select>
                        </div>

                        <div class="d-flex justify-content-end gap-2">
                            <button type="button" class="btn btn-teal btn-lg px-4 shadow-sm" id="btnProcessExit">
                                <i class="fas fa-check-circle me-2"></i>Confirm & Collect Payment
                            </button>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Receipt Modal -->
            <div class="modal fade" id="receiptModal" tabindex="-1">
                <div class="modal-dialog modal-dialog-centered">
                    <div class="modal-content border-0 shadow-lg">
                        <div class="modal-header bg-success text-white">
                            <h5 class="modal-title fw-bold"><i class="fas fa-receipt me-2"></i>Payment Receipt</h5>
                            <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal"></button>
                        </div>
                        <div class="modal-body p-4 text-center">
                            <div class="badge bg-light text-dark border px-3 py-2 font-monospace mb-3" id="recNumber">RCP-1001</div>
                            <h3 class="fw-bold text-success mb-3" id="recAmount">₹90</h3>
                            
                            <div class="p-3 bg-light rounded-3 border mb-3 text-start small">
                                <div class="d-flex justify-content-between py-1"><span>Ticket ID:</span><span id="recTicket"></span></div>
                                <div class="d-flex justify-content-between py-1"><span>Vehicle:</span><strong id="recVeh"></strong></div>
                                <div class="d-flex justify-content-between py-1"><span>Owner:</span><span id="recOwner"></span></div>
                                <div class="d-flex justify-content-between py-1"><span>Category:</span><span id="recType"></span></div>
                                <div class="d-flex justify-content-between py-1"><span>Bay(s):</span><span id="recSlot"></span></div>
                                <div class="d-flex justify-content-between py-1"><span>Billed Hours:</span><span id="recHours"></span></div>
                                <div class="d-flex justify-content-between py-1"><span>Method:</span><strong id="recMethod"></strong></div>
                                <div class="d-flex justify-content-between py-1"><span>Status:</span><span class="badge bg-success" id="recStatus">PAID</span></div>
                                <div class="d-flex justify-content-between py-1"><span>Timestamp:</span><span id="recTime"></span></div>
                            </div>
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-outline-secondary" data-bs-dismiss="modal">Close</button>
                            <button type="button" class="btn btn-teal" onclick="window.print()"><i class="fas fa-print me-1"></i> Print Receipt</button>
                        </div>
                    </div>
                </div>
            </div>
        </main>
    </div>
</div>
${getScripts(`
<script>
    document.addEventListener('DOMContentLoaded', () => {
        const urlParams = new URLSearchParams(window.location.search);
        const ticketParam = urlParams.get('ticket');
        if (ticketParam) {
            document.getElementById('exitSearchInput').value = ticketParam;
            document.getElementById('btnSearchTicket').click();
        }
    });
</script>
`)}
</body>
</html>`);

// ------------------------------------------------------------------------------
// Page 7: tickets.html
// ------------------------------------------------------------------------------
fs.writeFileSync(path.join(publicDir, 'tickets.html'), `<!DOCTYPE html>
<html lang="en">
<head>
    ${getHead('Parking Tickets')}
</head>
<body>
<div class="app-wrapper">
    ${getSidebar('tickets')}
    <div class="main-wrapper">
        ${getTopbar('Parking Tickets', 'Complete log of vehicle entries, exits, rates, and active tickets')}
        <main class="content-body">
            <div class="card card-custom p-4">
                <div class="d-flex flex-wrap justify-content-between align-items-center gap-3 mb-4">
                    <h5 class="fw-bold mb-0 text-dark">Ticket History Log</h5>
                    <div class="d-flex gap-2">
                        <select id="ticketStatusFilter" class="form-select form-select-sm" onchange="renderTickets()">
                            <option value="ALL">All Statuses</option>
                            <option value="ACTIVE" selected>Active Only</option>
                            <option value="COMPLETED">Completed Only</option>
                        </select>
                        <a href="/park-vehicle.html" class="btn btn-sm btn-teal">
                            <i class="fas fa-plus me-1"></i> New Ticket
                        </a>
                    </div>
                </div>

                <div class="table-responsive">
                    <table class="table table-hover align-middle mb-0">
                        <thead class="table-light">
                            <tr>
                                <th>Ticket ID</th>
                                <th>Vehicle Reg</th>
                                <th>Owner</th>
                                <th>Type</th>
                                <th>Bay(s)</th>
                                <th>Entry Time</th>
                                <th>Exit Time</th>
                                <th>Fee</th>
                                <th>Status</th>
                                <th>Action</th>
                            </tr>
                        </thead>
                        <tbody id="ticketsTableBody"></tbody>
                    </table>
                </div>
            </div>
        </main>
    </div>
</div>
${getScripts(`
<script>
    function renderTickets() {
        const db = ParkEaseDB.load();
        const filter = document.getElementById('ticketStatusFilter').value;
        const tbody = document.getElementById('ticketsTableBody');

        const tickets = db.tickets.filter(t => filter === 'ALL' || t.status === filter);
        if (tickets.length === 0) {
            tbody.innerHTML = '<tr><td colspan="10" class="text-center text-muted py-4">No tickets found for this filter.</td></tr>';
            return;
        }

        tbody.innerHTML = tickets.map(t => \`
            <tr>
                <td><span class="badge bg-light text-dark border font-monospace">\${t.ticketNumber}</span></td>
                <td><strong class="font-monospace">\${t.vehicleNumber}</strong></td>
                <td>\${t.ownerName}</td>
                <td><span class="badge bg-info text-dark">\${t.vehicleType}</span></td>
                <td><span class="badge bg-teal text-white">\${t.allocatedSlots}</span></td>
                <td class="small text-muted">\${new Date(t.entryTime).toLocaleString()}</td>
                <td class="small text-muted">\${t.exitTime ? new Date(t.exitTime).toLocaleString() : '<span class="text-warning">In Lot</span>'}</td>
                <td><strong>₹\${t.totalAmount || t.hourlyRate}</strong></td>
                <td>
                    <span class="badge \${t.status === 'ACTIVE' ? 'bg-warning text-dark' : 'bg-success'}">\${t.status}</span>
                </td>
                <td>
                    \${t.status === 'ACTIVE' ? \`
                        <a href="/vehicle-exit.html?ticket=\${t.ticketNumber}" class="btn btn-sm btn-outline-danger py-0 px-2">Exit & Pay</a>
                    \` : \`
                        <button class="btn btn-sm btn-outline-secondary py-0 px-2" onclick="alert('Ticket is completed and settled.')">Receipt</button>
                    \`}
                </td>
            </tr>
        \`).join('');
    }
    document.addEventListener('DOMContentLoaded', renderTickets);
</script>
`)}
</body>
</html>`);

// ------------------------------------------------------------------------------
// Page 8: vehicles.html
// ------------------------------------------------------------------------------
fs.writeFileSync(path.join(publicDir, 'vehicles.html'), `<!DOCTYPE html>
<html lang="en">
<head>
    ${getHead('Vehicle Management')}
</head>
<body>
<div class="app-wrapper">
    ${getSidebar('vehicles')}
    <div class="main-wrapper">
        ${getTopbar('Vehicle Management', 'Directory of vehicles registered in the ParkEase database')}
        <main class="content-body">
            <div class="card card-custom p-4">
                <div class="d-flex justify-content-between align-items-center mb-4">
                    <h5 class="fw-bold mb-0 text-dark">Registered Vehicles</h5>
                    <a href="/park-vehicle.html" class="btn btn-sm btn-teal"><i class="fas fa-plus me-1"></i> Register Vehicle</a>
                </div>
                <div class="table-responsive">
                    <table class="table table-hover align-middle mb-0">
                        <thead class="table-light">
                            <tr>
                                <th>Registration No.</th>
                                <th>Owner Name</th>
                                <th>Contact</th>
                                <th>Type</th>
                                <th>Current Slot</th>
                                <th>Parking Status</th>
                            </tr>
                        </thead>
                        <tbody id="vehiclesTableBody"></tbody>
                    </table>
                </div>
            </div>
        </main>
    </div>
</div>
${getScripts(`
<script>
    function renderVehicles() {
        const db = ParkEaseDB.load();
        const tbody = document.getElementById('vehiclesTableBody');
        
        // Group distinct vehicles from tickets
        const map = new Map();
        db.tickets.forEach(t => {
            if (!map.has(t.vehicleNumber)) {
                map.set(t.vehicleNumber, {
                    vehicleNumber: t.vehicleNumber,
                    ownerName: t.ownerName,
                    contactNumber: t.contactNumber,
                    vehicleType: t.vehicleType,
                    status: t.status === 'ACTIVE' ? 'PARKED' : 'EXITED',
                    slot: t.status === 'ACTIVE' ? t.allocatedSlots : 'None'
                });
            } else if (t.status === 'ACTIVE') {
                map.get(t.vehicleNumber).status = 'PARKED';
                map.get(t.vehicleNumber).slot = t.allocatedSlots;
            }
        });

        const list = Array.from(map.values());
        tbody.innerHTML = list.map(v => \`
            <tr>
                <td><strong class="font-monospace">\${v.vehicleNumber}</strong></td>
                <td>\${v.ownerName}</td>
                <td>\${v.contactNumber || 'N/A'}</td>
                <td><span class="badge bg-info text-dark">\${v.vehicleType}</span></td>
                <td><span class="badge \${v.slot !== 'None' ? 'bg-teal text-white' : 'bg-light text-muted border'}">\${v.slot}</span></td>
                <td><span class="badge \${v.status === 'PARKED' ? 'bg-danger' : 'bg-secondary'}">\${v.status}</span></td>
            </tr>
        \`).join('');
    }
    document.addEventListener('DOMContentLoaded', renderVehicles);
</script>
`)}
</body>
</html>`);

// ------------------------------------------------------------------------------
// Page 9: billing.html
// ------------------------------------------------------------------------------
fs.writeFileSync(path.join(publicDir, 'billing.html'), `<!DOCTYPE html>
<html lang="en">
<head>
    ${getHead('Billing & Payments')}
</head>
<body>
<div class="app-wrapper">
    ${getSidebar('billing')}
    <div class="main-wrapper">
        ${getTopbar('Billing & Payments', 'Transaction receipts, financial audit logs, and revenue breakdown')}
        <main class="content-body">
            <div class="card card-custom p-4">
                <div class="d-flex justify-content-between align-items-center mb-4">
                    <h5 class="fw-bold mb-0 text-dark">Payment Transactions Log</h5>
                    <button class="btn btn-sm btn-outline-secondary" onclick="alert('Exported receipts successfully!')">
                        <i class="fas fa-download me-1"></i> Export CSV
                    </button>
                </div>
                <div class="table-responsive">
                    <table class="table table-hover align-middle mb-0">
                        <thead class="table-light">
                            <tr>
                                <th>Receipt ID</th>
                                <th>Ticket ID</th>
                                <th>Vehicle Reg</th>
                                <th>Owner</th>
                                <th>Billed Hours</th>
                                <th>Amount</th>
                                <th>Payment Method</th>
                                <th>Transaction Ref</th>
                                <th>Payment Time</th>
                            </tr>
                        </thead>
                        <tbody id="billingTableBody"></tbody>
                    </table>
                </div>
            </div>
        </main>
    </div>
</div>
${getScripts(`
<script>
    function renderBilling() {
        const db = ParkEaseDB.load();
        const tbody = document.getElementById('billingTableBody');
        const payments = db.payments;

        if (payments.length === 0) {
            tbody.innerHTML = '<tr><td colspan="9" class="text-center text-muted py-4">No completed payments yet.</td></tr>';
            return;
        }

        tbody.innerHTML = payments.map(p => \`
            <tr>
                <td><span class="badge bg-light text-dark border font-monospace">\${p.receiptNumber}</span></td>
                <td><span class="font-monospace">\${p.ticketNumber}</span></td>
                <td><strong class="font-monospace">\${p.vehicleNumber}</strong></td>
                <td>\${p.ownerName}</td>
                <td>\${p.billedHours} hrs</td>
                <td><strong class="text-teal">₹\${p.totalAmount}</strong></td>
                <td><span class="badge bg-primary">\${p.paymentMethod}</span></td>
                <td class="font-monospace small text-muted">\${p.transactionRef}</td>
                <td class="small text-muted">\${new Date(p.paymentTime).toLocaleString()}</td>
            </tr>
        \`).join('');
    }
    document.addEventListener('DOMContentLoaded', renderBilling);
</script>
`)}
</body>
</html>`);

// ------------------------------------------------------------------------------
// Page 10: reports.html
// ------------------------------------------------------------------------------
fs.writeFileSync(path.join(publicDir, 'reports.html'), `<!DOCTYPE html>
<html lang="en">
<head>
    ${getHead('Reports & Analytics')}
</head>
<body>
<div class="app-wrapper">
    ${getSidebar('reports')}
    <div class="main-wrapper">
        ${getTopbar('Reports & Analytics', 'Occupancy statistics, revenue trends, and operational insights')}
        <main class="content-body">
            <div class="row g-4 mb-4">
                <div class="col-lg-6">
                    <div class="card card-custom p-4 h-100">
                        <h5 class="fw-bold mb-3 text-dark">Weekly Revenue Trend</h5>
                        <div style="height: 280px;"><canvas id="weeklyChart"></canvas></div>
                    </div>
                </div>
                <div class="col-lg-6">
                    <div class="card card-custom p-4 h-100">
                        <h5 class="fw-bold mb-3 text-dark">Slot Occupancy Breakdown</h5>
                        <div style="height: 280px;"><canvas id="occupancyChart"></canvas></div>
                    </div>
                </div>
            </div>
        </main>
    </div>
</div>
${getScripts(`
<script>
    document.addEventListener('DOMContentLoaded', () => {
        const stats = ParkEaseDB.getStats();
        new Chart(document.getElementById('weeklyChart'), {
            type: 'line',
            data: {
                labels: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun'],
                datasets: [{
                    label: 'Revenue (₹)',
                    data: [120, 250, 180, 310, 270, 410, stats.totalRevenue],
                    borderColor: '#0d9488',
                    backgroundColor: 'rgba(13, 148, 136, 0.1)',
                    fill: true,
                    tension: 0.3
                }]
            },
            options: { responsive: true, maintainAspectRatio: false }
        });

        new Chart(document.getElementById('occupancyChart'), {
            type: 'pie',
            data: {
                labels: ['Available', 'Occupied'],
                datasets: [{
                    data: [stats.availableSlots, stats.occupiedSlots],
                    backgroundColor: ['#10b981', '#ef4444']
                }]
            },
            options: { responsive: true, maintainAspectRatio: false }
        });
    });
</script>
`)}
</body>
</html>`);

// ------------------------------------------------------------------------------
// Page 11: database-records.html
// ------------------------------------------------------------------------------
fs.writeFileSync(path.join(publicDir, 'database-records.html'), `<!DOCTYPE html>
<html lang="en">
<head>
    ${getHead('Database Records')}
</head>
<body>
<div class="app-wrapper">
    ${getSidebar('database-records')}
    <div class="main-wrapper">
        ${getTopbar('Database Records', 'Live SQL relational tables view matching MySQL parkease_db schema')}
        <main class="content-body">
            <div class="card card-custom p-4">
                <ul class="nav nav-tabs mb-4" id="dbTabs">
                    <li class="nav-item"><a class="nav-link active" onclick="switchTable('slots')">parking_slots (20)</a></li>
                    <li class="nav-item"><a class="nav-link" onclick="switchTable('tickets')">parking_tickets</a></li>
                    <li class="nav-item"><a class="nav-link" onclick="switchTable('payments')">payments</a></li>
                    <li class="nav-item"><a class="nav-link" onclick="switchTable('rates')">vehicle_rates</a></li>
                    <li class="nav-item"><a class="nav-link" onclick="switchTable('users')">users</a></li>
                </ul>

                <div class="table-responsive">
                    <table class="table table-bordered table-sm font-monospace small align-middle mb-0" id="sqlTable">
                        <thead class="table-dark" id="sqlThead"></thead>
                        <tbody id="sqlTbody"></tbody>
                    </table>
                </div>
            </div>
        </main>
    </div>
</div>
${getScripts(`
<script>
    let currentTable = 'slots';

    function switchTable(name) {
        currentTable = name;
        document.querySelectorAll('#dbTabs .nav-link').forEach(l => l.classList.remove('active'));
        event.target.classList.add('active');
        renderSqlTable();
    }

    function renderSqlTable() {
        const db = ParkEaseDB.load();
        const thead = document.getElementById('sqlThead');
        const tbody = document.getElementById('sqlTbody');

        if (currentTable === 'slots') {
            thead.innerHTML = '<tr><th>id</th><th>slot_number</th><th>status</th><th>vehicle_number</th><th>owner_name</th><th>ticket_number</th></tr>';
            tbody.innerHTML = db.slots.map(s => \`
                <tr>
                    <td>\${s.id}</td>
                    <td><strong>\${s.slotNumber}</strong></td>
                    <td><span class="badge \${s.status === 'OCCUPIED' ? 'bg-danger' : 'bg-success'}">\${s.status}</span></td>
                    <td>\${s.vehicleNumber || 'NULL'}</td>
                    <td>\${s.ownerName || 'NULL'}</td>
                    <td>\${s.ticketNumber || 'NULL'}</td>
                </tr>
            \`).join('');
        } else if (currentTable === 'tickets') {
            thead.innerHTML = '<tr><th>ticket_number</th><th>vehicle_number</th><th>owner_name</th><th>type</th><th>slots</th><th>entry_time</th><th>rate</th><th>status</th></tr>';
            tbody.innerHTML = db.tickets.map(t => \`
                <tr>
                    <td><strong>\${t.ticketNumber}</strong></td>
                    <td>\${t.vehicleNumber}</td>
                    <td>\${t.ownerName}</td>
                    <td>\${t.vehicleType}</td>
                    <td>\${t.allocatedSlots}</td>
                    <td>\${t.entryTime}</td>
                    <td>\${t.hourlyRate}</td>
                    <td><span class="badge \${t.status === 'ACTIVE' ? 'bg-warning text-dark' : 'bg-success'}">\${t.status}</span></td>
                </tr>
            \`).join('');
        } else if (currentTable === 'payments') {
            thead.innerHTML = '<tr><th>receipt_number</th><th>ticket_number</th><th>amount</th><th>method</th><th>status</th><th>ref</th><th>time</th></tr>';
            tbody.innerHTML = db.payments.map(p => \`
                <tr>
                    <td><strong>\${p.receiptNumber}</strong></td>
                    <td>\${p.ticketNumber}</td>
                    <td>₹\${p.totalAmount}</td>
                    <td>\${p.paymentMethod}</td>
                    <td>\${p.paymentStatus}</td>
                    <td>\${p.transactionRef}</td>
                    <td>\${p.paymentTime}</td>
                </tr>
            \`).join('');
        } else if (currentTable === 'rates') {
            thead.innerHTML = '<tr><th>vehicle_type</th><th>display_name</th><th>hourly_rate</th><th>slots_required</th></tr>';
            tbody.innerHTML = Object.values(db.rates).map(r => \`
                <tr>
                    <td><strong>\${r.type}</strong></td>
                    <td>\${r.name}</td>
                    <td>₹\${r.rate}/hr</td>
                    <td>\${r.bays}</td>
                </tr>
            \`).join('');
        } else if (currentTable === 'users') {
            thead.innerHTML = '<tr><th>id</th><th>username</th><th>full_name</th><th>role</th><th>created_at</th></tr>';
            tbody.innerHTML = db.users.map(u => \`
                <tr>
                    <td>\${u.id}</td>
                    <td><strong>\${u.username}</strong></td>
                    <td>\${u.fullName}</td>
                    <td><span class="badge bg-primary">\${u.role}</span></td>
                    <td>\${u.createdAt}</td>
                </tr>
            \`).join('');
        }
    }

    document.addEventListener('DOMContentLoaded', renderSqlTable);
</script>
`)}
</body>
</html>`);

// ------------------------------------------------------------------------------
// Page 12: settings.html
// ------------------------------------------------------------------------------
fs.writeFileSync(path.join(publicDir, 'settings.html'), `<!DOCTYPE html>
<html lang="en">
<head>
    ${getHead('Settings')}
</head>
<body>
<div class="app-wrapper">
    ${getSidebar('settings')}
    <div class="main-wrapper">
        ${getTopbar('Settings', 'System configuration, parking tariffs, and database resets')}
        <main class="content-body">
            <div class="card card-custom p-4 mb-4">
                <h5 class="fw-bold mb-3 text-dark">Tariff &amp; Billing Rates</h5>
                <div class="row g-3">
                    <div class="col-md-4">
                        <label class="form-label small text-muted">Bike Hourly Rate</label>
                        <div class="input-group">
                            <span class="input-group-text">₹</span>
                            <input type="number" id="rateBike" class="form-control" value="10">
                            <span class="input-group-text">/hr</span>
                        </div>
                    </div>
                    <div class="col-md-4">
                        <label class="form-label small text-muted">Car Hourly Rate</label>
                        <div class="input-group">
                            <span class="input-group-text">₹</span>
                            <input type="number" id="rateCar" class="form-control" value="30">
                            <span class="input-group-text">/hr</span>
                        </div>
                    </div>
                    <div class="col-md-4">
                        <label class="form-label small text-muted">Truck Hourly Rate</label>
                        <div class="input-group">
                            <span class="input-group-text">₹</span>
                            <input type="number" id="rateTruck" class="form-control" value="60">
                            <span class="input-group-text">/hr</span>
                        </div>
                    </div>
                    <div class="col-md-12">
                        <label class="form-label small text-muted">Maximum Daily Billing Hours Cap</label>
                        <input type="number" id="maxCap" class="form-control" value="8" style="max-width: 200px;">
                        <div class="form-text small">Daily charge ceiling: Customers are billed for a maximum of 8 hours per session.</div>
                    </div>
                </div>
                <div class="mt-4">
                    <button class="btn btn-teal" onclick="saveSettings()"><i class="fas fa-save me-1"></i> Save Changes</button>
                </div>
            </div>

            <div class="card card-custom p-4 border-danger">
                <h5 class="fw-bold mb-2 text-danger">Reset Database / Seed Data</h5>
                <p class="text-muted small mb-3">Restore initial 20 slots, default test tickets, and seed records.</p>
                <button class="btn btn-outline-danger" onclick="resetDb()"><i class="fas fa-undo me-1"></i> Reset to Default State</button>
            </div>
        </main>
    </div>
</div>
${getScripts(`
<script>
    function saveSettings() {
        const db = ParkEaseDB.load();
        db.rates.BIKE.rate = parseInt(document.getElementById('rateBike').value) || 10;
        db.rates.CAR.rate = parseInt(document.getElementById('rateCar').value) || 30;
        db.rates.TRUCK.rate = parseInt(document.getElementById('rateTruck').value) || 60;
        db.settings.maxDailyHoursCap = parseInt(document.getElementById('maxCap').value) || 8;
        ParkEaseDB.save(db);
        alert('Settings saved successfully!');
    }

    function resetDb() {
        if (confirm('Are you sure you want to restore the initial database state?')) {
            ParkEaseDB.reset();
            alert('Database reset to original 20 slots and seed records!');
            window.location.reload();
        }
    }
</script>
`)}
</body>
</html>`);

console.log('All 12 static HTML pages successfully generated in public/!');
