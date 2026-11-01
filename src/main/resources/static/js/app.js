/**
 * ParkEase - Client Application JavaScript
 * Handles AJAX forms, Live Slot Updates, Ticket & Receipt Printing, and Modals
 */

document.addEventListener('DOMContentLoaded', () => {
    initParkForm();
    initExitModule();
    initRateCards();
});

// ------------------------------------------------------------------------------
// Park Vehicle Module
// ------------------------------------------------------------------------------
function initParkForm() {
    const form = document.getElementById('parkVehicleForm');
    if (!form) return;

    form.addEventListener('submit', async (e) => {
        e.preventDefault();

        const submitBtn = form.querySelector('button[type="submit"]');
        const originalBtnText = submitBtn.innerHTML;
        submitBtn.disabled = true;
        submitBtn.innerHTML = '<span class="spinner-border spinner-border-sm me-2"></span>Allocating Slot...';

        const payload = {
            ownerName: document.getElementById('ownerName').value.trim(),
            vehicleNumber: document.getElementById('vehicleNumber').value.trim().toUpperCase(),
            vehicleType: document.getElementById('vehicleType').value,
            contactNumber: document.getElementById('contactNumber').value.trim(),
            customEntryTime: document.getElementById('customEntryTime')?.value || null
        };

        try {
            const resp = await fetch('/api/park', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(payload)
            });

            const data = await resp.json();

            if (resp.ok && data.success) {
                // Populate Printable Ticket Modal
                document.getElementById('modalTicketNum').innerText = data.ticketNumber;
                document.getElementById('modalVehNum').innerText = data.vehicleNumber;
                document.getElementById('modalOwner').innerText = data.ownerName;
                document.getElementById('modalType').innerText = data.vehicleType;
                document.getElementById('modalSlot').innerText = data.allocatedSlots;
                document.getElementById('modalEntryTime').innerText = new Date(data.entryTime).toLocaleString();
                document.getElementById('modalRate').innerText = '₹' + data.hourlyRate + '/hr';

                form.reset();

                const ticketModal = new bootstrap.Modal(document.getElementById('ticketModal'));
                ticketModal.show();
            } else {
                showToast(data.message || 'Failed to allocate parking slot.', 'danger');
            }
        } catch (err) {
            showToast('Network error or server unavailable. Please try again.', 'danger');
            console.error(err);
        } finally {
            submitBtn.disabled = false;
            submitBtn.innerHTML = originalBtnText;
        }
    });
}

// ------------------------------------------------------------------------------
// Vehicle Exit Module
// ------------------------------------------------------------------------------
function initExitModule() {
    const searchBtn = document.getElementById('btnSearchTicket');
    const searchInput = document.getElementById('exitSearchInput');

    if (searchBtn && searchInput) {
        const doSearch = async () => {
            const val = searchInput.value.trim();
            if (!val) {
                showToast('Please enter a Ticket ID or Vehicle Registration Number', 'warning');
                return;
            }

            searchBtn.disabled = true;
            searchBtn.innerHTML = '<span class="spinner-border spinner-border-sm"></span>';

            try {
                const resp = await fetch(`/api/exit/calculate?search=${encodeURIComponent(val)}`);
                const data = await resp.json();

                if (resp.ok && data.ticketNumber) {
                    renderExitCalculation(data);
                } else {
                    document.getElementById('exitResultCard').classList.add('d-none');
                    showToast(data.message || 'No active ticket found for ' + val, 'danger');
                }
            } catch (err) {
                showToast('Failed to retrieve ticket information.', 'danger');
            } finally {
                searchBtn.disabled = false;
                searchBtn.innerHTML = '<i class="fas fa-search me-2"></i>Search';
            }
        };

        searchBtn.addEventListener('click', doSearch);
        searchInput.addEventListener('keypress', (e) => {
            if (e.key === 'Enter') {
                e.preventDefault();
                doSearch();
            }
        });
    }

    // Process Exit & Payment
    const processExitBtn = document.getElementById('btnProcessExit');
    if (processExitBtn) {
        processExitBtn.addEventListener('click', async () => {
            const ticketNumber = document.getElementById('calcTicketNumber').innerText;
            const paymentMethod = document.getElementById('exitPaymentMethod').value;
            const amount = parseFloat(document.getElementById('calcFinalAmount').innerText.replace('₹', ''));

            processExitBtn.disabled = true;
            processExitBtn.innerHTML = '<span class="spinner-border spinner-border-sm me-2"></span>Processing Payment...';

            try {
                const resp = await fetch('/api/exit/process', {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify({
                        ticketNumber: ticketNumber,
                        paymentMethod: paymentMethod,
                        amountPaid: amount
                    })
                });

                const data = await resp.json();

                if (resp.ok && data.receiptNumber) {
                    document.getElementById('exitResultCard').classList.add('d-none');
                    if (searchInput) searchInput.value = '';

                    // Show Receipt Modal
                    renderReceiptModal(data);
                } else {
                    showToast(data.message || 'Payment processing failed.', 'danger');
                }
            } catch (err) {
                showToast('Payment processing error.', 'danger');
            } finally {
                processExitBtn.disabled = false;
                processExitBtn.innerHTML = '<i class="fas fa-check-circle me-2"></i>Confirm & Collect Payment';
            }
        });
    }
}

function renderExitCalculation(data) {
    const card = document.getElementById('exitResultCard');
    if (!card) return;

    document.getElementById('calcTicketNumber').innerText = data.ticketNumber;
    document.getElementById('calcVehNumber').innerText = data.vehicleNumber;
    document.getElementById('calcOwner').innerText = data.ownerName;
    document.getElementById('calcType').innerText = data.vehicleType;
    document.getElementById('calcSlot').innerText = data.allocatedSlots;
    document.getElementById('calcEntryTime').innerText = new Date(data.entryTime).toLocaleString();
    document.getElementById('calcExitTime').innerText = new Date(data.exitTime).toLocaleString();
    document.getElementById('calcDuration').innerText = data.formattedDuration + ' (' + data.billedHours + ' billed hrs)';
    document.getElementById('calcRate').innerText = '₹' + data.hourlyRate + '/hr';
    document.getElementById('calcFinalAmount').innerText = '₹' + data.totalAmount;

    const capBadge = document.getElementById('calcCapBadge');
    if (data.dailyCapApplied) {
        capBadge.classList.remove('d-none');
    } else {
        capBadge.classList.add('d-none');
    }

    card.classList.remove('d-none');
    card.scrollIntoView({ behavior: 'smooth' });
}

function renderReceiptModal(receipt) {
    document.getElementById('recNumber').innerText = receipt.receiptNumber;
    document.getElementById('recTicket').innerText = receipt.ticketNumber;
    document.getElementById('recVeh').innerText = receipt.vehicleNumber;
    document.getElementById('recOwner').innerText = receipt.ownerName;
    document.getElementById('recType').innerText = receipt.vehicleType;
    document.getElementById('recSlot').innerText = receipt.allocatedSlots;
    document.getElementById('recEntry').innerText = new Date(receipt.entryTime).toLocaleString();
    document.getElementById('recExit').innerText = new Date(receipt.exitTime).toLocaleString();
    document.getElementById('recHours').innerText = receipt.billedHours + ' hrs';
    document.getElementById('recRate').innerText = '₹' + receipt.hourlyRate + '/hr';
    document.getElementById('recAmount').innerText = '₹' + receipt.totalAmount;
    document.getElementById('recMethod').innerText = receipt.paymentMethod;
    document.getElementById('recStatus').innerText = receipt.paymentStatus;
    document.getElementById('recTime').innerText = new Date(receipt.paymentTime).toLocaleString();

    const receiptModal = new bootstrap.Modal(document.getElementById('receiptModal'));
    receiptModal.show();
}

function initRateCards() {
    const rateSelect = document.getElementById('vehicleType');
    if (!rateSelect) return;

    rateSelect.addEventListener('change', () => {
        const val = rateSelect.value;
        const hint = document.getElementById('rateHint');
        if (!hint) return;

        if (val === 'BIKE') {
            hint.innerHTML = '<span class="text-teal fw-bold">₹10/hr</span> • Allocates 1 slot';
        } else if (val === 'CAR') {
            hint.innerHTML = '<span class="text-teal fw-bold">₹30/hr</span> • Allocates 1 slot';
        } else if (val === 'TRUCK') {
            hint.innerHTML = '<span class="text-teal fw-bold">₹60/hr</span> • Allocates <span class="badge bg-warning text-dark">2 Consecutive Slots</span>';
        }
    });
}

// ------------------------------------------------------------------------------
// Toast Notification Utility
// ------------------------------------------------------------------------------
function showToast(message, type = 'info') {
    let container = document.getElementById('toastContainer');
    if (!container) {
        container = document.createElement('div');
        container.id = 'toastContainer';
        container.className = 'toast-container position-fixed bottom-0 end-0 p-3';
        container.style.zIndex = '9999';
        document.body.appendChild(container);
    }

    const toastEl = document.createElement('div');
    toastEl.className = `toast align-items-center text-white bg-${type} border-0 shadow-lg`;
    toastEl.setAttribute('role', 'alert');
    toastEl.setAttribute('aria-live', 'assertive');
    toastEl.setAttribute('aria-atomic', 'true');

    toastEl.innerHTML = `
        <div class="d-flex">
            <div class="toast-body fs-6">
                <i class="fas ${type === 'danger' ? 'fa-exclamation-circle' : type === 'warning' ? 'fa-exclamation-triangle' : 'fa-info-circle'} me-2"></i>
                ${message}
            </div>
            <button type="button" class="btn-close btn-close-white me-2 m-auto" data-bs-dismiss="toast" aria-label="Close"></button>
        </div>
    `;

    container.appendChild(toastEl);
    const toast = new bootstrap.Toast(toastEl, { delay: 4500 });
    toast.show();

    toastEl.addEventListener('hidden.bs.toast', () => {
        toastEl.remove();
    });
}

