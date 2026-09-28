// Simple API call helper
async function callBackend(endpoint, method, bodyData) {
    const options = {
        method: method,
        headers: { 'Content-Type': 'application/json' }
    };
    if (bodyData) {
        options.body = JSON.stringify(bodyData);
    }
    const response = await fetch('/api' + endpoint, options);
    return await response.json();
}

// 1. Load data when page opens
window.onload = function() {
    loadCrops();
    if(document.getElementById('stockTable')) {
        loadStockTable();
    }
};

// 2. Load Crops into the Dropdown menus
async function loadCrops() {
    const crops = await callBackend('/crops', 'GET');
    
    let htmlOptions = '<option value="">Select a crop</option>';
    let revHtmlOptions = '<option value="all">All Crops</option>';

    for (let i = 0; i < crops.length; i++) {
        htmlOptions += '<option value="' + crops[i].id + '">' + crops[i].name + '</option>';
        revHtmlOptions += '<option value="' + crops[i].id + '">' + crops[i].name + '</option>';
    }

    if(document.getElementById('harvestCropSelect')) document.getElementById('harvestCropSelect').innerHTML = htmlOptions;
    if(document.getElementById('saleCropSelect')) document.getElementById('saleCropSelect').innerHTML = htmlOptions;
    if(document.getElementById('revCropSelect')) document.getElementById('revCropSelect').innerHTML = revHtmlOptions;
}

// 3. Load Stock Table
async function loadStockTable() {
    if(!document.getElementById('stockTable')) return;
    const stockData = await callBackend('/stock', 'GET');
    let tableRows = '';

    for (let i = 0; i < stockData.length; i++) {
        tableRows += '<tr>';
        tableRows += '<td>' + stockData[i].cropName + '</td>';
        tableRows += '<td>' + stockData[i].harvested + '</td>';
        tableRows += '<td>' + stockData[i].sold + '</td>';
        tableRows += '<td>' + stockData[i].stock + '</td>';
        tableRows += '</tr>';
    }

    document.querySelector('#stockTable tbody').innerHTML = tableRows;
}

// 4. Add New Crop form submission
if(document.getElementById('addCropForm')) {
    document.getElementById('addCropForm').onsubmit = async function(event) {
        event.preventDefault(); // Stop page refresh
        const newCropName = document.getElementById('cropName').value;
        
        await callBackend('/crops', 'POST', { name: newCropName, unit: 'kg' });
        
        alert('Crop added successfully!');
        document.getElementById('addCropForm').reset();
        loadCrops(); // Refresh dropdowns
    };
}

// 5. Record Harvest form submission
if(document.getElementById('recordHarvestForm')) {
    document.getElementById('recordHarvestForm').onsubmit = async function(event) {
        event.preventDefault();
        
        const requestData = {
            cropId: document.getElementById('harvestCropSelect').value,
            quantity: document.getElementById('harvestQuantity').value,
            harvestDate: document.getElementById('harvestDate').value
        };

        await callBackend('/harvests', 'POST', requestData);
        alert('Harvest saved!');
        document.getElementById('recordHarvestForm').reset();
    };
}

// 6. Record Sale form submission
if(document.getElementById('recordSaleForm')) {
    document.getElementById('recordSaleForm').onsubmit = async function(event) {
        event.preventDefault();

        const requestData = {
            cropId: document.getElementById('saleCropSelect').value,
            quantity: document.getElementById('saleQuantity').value,
            pricePerUnit: document.getElementById('salePrice').value,
            saleDate: document.getElementById('saleDate').value
        };

        await callBackend('/sales', 'POST', requestData);
        alert('Sale saved!');
        document.getElementById('recordSaleForm').reset();
    };
}

// 7. Revenue Report form submission
if(document.getElementById('revenueForm')) {
    document.getElementById('revenueForm').onsubmit = async function(event) {
        event.preventDefault();

        const fromDate = document.getElementById('revFromDate').value;
        const toDate = document.getElementById('revToDate').value;
        const cropId = document.getElementById('revCropSelect').value;

        let revenueData = await callBackend('/revenue?from=' + fromDate + '&to=' + toDate, 'GET');
        
        // Filter if specific crop selected
        if (cropId !== 'all') {
            revenueData = revenueData.filter(function(record) {
                return String(record.cropId) === cropId;
            });
        }

        let tableRows = '';
        let totalAmount = 0;

        for (let i = 0; i < revenueData.length; i++) {
            totalAmount += revenueData[i].revenue;
            tableRows += '<tr>';
            tableRows += '<td>' + revenueData[i].cropName + '</td>';
            tableRows += '<td>&#8377;' + revenueData[i].revenue + '</td>';
            tableRows += '</tr>';
        }

        document.querySelector('#revenueTable tbody').innerHTML = tableRows;
        document.getElementById('totalRevAmount').innerHTML = '&#8377;' + totalAmount;
        document.getElementById('revenueResult').style.display = 'block';
    };
}
