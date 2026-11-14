(() => {
  'use strict';

  const STORAGE_KEYS = {
    PRODUCTS: 'gramkart_products_v1',
    ORDERS: 'gramkart_orders_v1',
    CART: 'gramkart_cart_v1',
    PROFILE: 'gramkart_profile_v1',
    LANG: 'gramkart_lang_v1',
    ADMIN_AUTH: 'gramkart_admin_auth_v1'
  };

  const CATEGORIES = [
    { id: 'GROCERY', en: 'Grocery', hi: 'किराना', emoji: '🌾', bg: '#FEF3C7', fg: '#B45309' },
    { id: 'VEGETABLES', en: 'Vegetables', hi: 'सब्ज़ियाँ', emoji: '🍅', bg: '#DCFCE7', fg: '#15803D' },
    { id: 'FRUITS', en: 'Fruits', hi: 'फल', emoji: '🥭', bg: '#FFEDD5', fg: '#EA580C' },
    { id: 'DAIRY', en: 'Dairy', hi: 'डेयरी', emoji: '🥛', bg: '#E0F2FE', fg: '#0284C7' },
    { id: 'MEDICAL', en: 'Medical', hi: 'मेडिकल', emoji: '💊', bg: '#FEE2E2', fg: '#DC2626' },
    { id: 'HOUSEHOLD', en: 'Household', hi: 'घरेलू', emoji: '🧼', bg: '#EDE9FE', fg: '#7C3AED' }
  ];

  const ORDER_STATUSES = [
    { id: 'PENDING', en: 'Pending', hi: 'लंबित' },
    { id: 'CONFIRMED', en: 'Confirmed', hi: 'कन्फर्म' },
    { id: 'PREPARING', en: 'Preparing', hi: 'तैयारी हो रही' },
    { id: 'OUT_FOR_DELIVERY', en: 'Out for Delivery', hi: 'डिलीवरी के लिए निकला' },
    { id: 'DELIVERED', en: 'Delivered', hi: 'डिलीवर हो गया' },
    { id: 'CANCELLED', en: 'Cancelled', hi: 'रद्द' }
  ];

  const ART_EMOJIS = {
    rice: '🍚', atta: '🌾', dal: '🫘', oil: '🫙',
    tomato: '🍅', potato: '🥔', onion: '🧅', spinach: '🥬',
    mango: '🥭', apple: '🍎', banana: '🍌',
    milk: '🥛', paneer: '🧀', curd: '🥣',
    medicine: '💊', bandage: '🩹', sanitizer: '🧴',
    detergent: '🧺', dishwash: '🍋', soap: '🧼'
  };

  const DEFAULT_PRODUCTS = [
    { id: 1, nameEn: 'Royal Basmati Rice', nameHi: 'रॉयल बासमती चावल', category: 'GROCERY', priceInr: 128, mrpInr: 150, unitEn: '1 kg', unitHi: '1 किग्रा', stock: 24, imageKey: 'rice' },
    { id: 2, nameEn: 'Whole Wheat Chakki Atta', nameHi: 'चक्की फ्रेश आटा', category: 'GROCERY', priceInr: 245, mrpInr: 275, unitEn: '5 kg', unitHi: '5 किग्रा', stock: 16, imageKey: 'atta' },
    { id: 3, nameEn: 'Unpolished Toor Dal', nameHi: 'अरहर / तूर दाल', category: 'GROCERY', priceInr: 165, mrpInr: 185, unitEn: '1 kg', unitHi: '1 किग्रा', stock: 18, imageKey: 'dal' },
    { id: 4, nameEn: 'Kachi Ghani Mustard Oil', nameHi: 'कच्ची घानी सरसों तेल', category: 'GROCERY', priceInr: 158, mrpInr: 175, unitEn: '1 L', unitHi: '1 लीटर', stock: 14, imageKey: 'oil' },
    { id: 5, nameEn: 'Fresh Desi Tomatoes', nameHi: 'ताज़ा देसी टमाटर', category: 'VEGETABLES', priceInr: 38, mrpInr: 48, unitEn: '1 kg', unitHi: '1 किग्रा', stock: 30, imageKey: 'tomato' },
    { id: 6, nameEn: 'Organic Potatoes (Aloo)', nameHi: 'ताज़ा आलू', category: 'VEGETABLES', priceInr: 32, mrpInr: 40, unitEn: '1 kg', unitHi: '1 किग्रा', stock: 40, imageKey: 'potato' },
    { id: 7, nameEn: 'Red Nashik Onions', nameHi: 'लाल प्याज़', category: 'VEGETABLES', priceInr: 44, mrpInr: 54, unitEn: '1 kg', unitHi: '1 किग्रा', stock: 28, imageKey: 'onion' },
    { id: 8, nameEn: 'Fresh Green Spinach', nameHi: 'ताज़ा हरा पालक', category: 'VEGETABLES', priceInr: 22, mrpInr: 28, unitEn: '250 g', unitHi: '250 ग्राम', stock: 12, imageKey: 'spinach' },
    { id: 9, nameEn: 'Sweet Kesar Mangoes', nameHi: 'मीठे केसर आम', category: 'FRUITS', priceInr: 145, mrpInr: 175, unitEn: '1 kg', unitHi: '1 किग्रा', stock: 15, imageKey: 'mango' },
    { id: 10, nameEn: 'Kashmir Red Apples', nameHi: 'कश्मीरी लाल सेब', category: 'FRUITS', priceInr: 160, mrpInr: 190, unitEn: '1 kg', unitHi: '1 किग्रा', stock: 14, imageKey: 'apple' },
    { id: 11, nameEn: 'Fresh Robusta Bananas', nameHi: 'ताज़े केले', category: 'FRUITS', priceInr: 36, mrpInr: 45, unitEn: '6 pcs', unitHi: '6 नग', stock: 22, imageKey: 'banana' },
    { id: 12, nameEn: 'Farm Fresh Toned Milk', nameHi: 'ताज़ा टोंड दूध', category: 'DAIRY', priceInr: 54, mrpInr: 58, unitEn: '1 L', unitHi: '1 लीटर', stock: 25, imageKey: 'milk' },
    { id: 13, nameEn: 'Soft Malai Paneer', nameHi: 'ताज़ा मलाई पनीर', category: 'DAIRY', priceInr: 88, mrpInr: 98, unitEn: '200 g', unitHi: '200 ग्राम', stock: 4, imageKey: 'paneer' },
    { id: 14, nameEn: 'Thick Homestead Curd', nameHi: 'गाढ़ा ताज़ा दही', category: 'DAIRY', priceInr: 38, mrpInr: 42, unitEn: '400 g', unitHi: '400 ग्राम', stock: 18, imageKey: 'curd' },
    { id: 15, nameEn: 'Paracetamol 500mg', nameHi: 'पैरासिटामोल 500mg', category: 'MEDICAL', priceInr: 28, mrpInr: 32, unitEn: '15 tabs', unitHi: '15 गोलियाँ', stock: 35, imageKey: 'medicine' },
    { id: 16, nameEn: 'First-Aid Bandage Pack', nameHi: 'फर्स्ट-एड बैंडेज पैक', category: 'MEDICAL', priceInr: 45, mrpInr: 55, unitEn: '20 strips', unitHi: '20 पट्टियाँ', stock: 20, imageKey: 'bandage' },
    { id: 17, nameEn: 'Herbal Hand Sanitizer', nameHi: 'हर्बल हैंड सैनिटाइज़र', category: 'MEDICAL', priceInr: 79, mrpInr: 95, unitEn: '200 ml', unitHi: '200 मिली', stock: 3, imageKey: 'sanitizer' },
    { id: 18, nameEn: 'Active Wash Detergent', nameHi: 'एक्टिव वॉश डिटर्जेंट', category: 'HOUSEHOLD', priceInr: 132, mrpInr: 150, unitEn: '1 kg', unitHi: '1 किग्रा', stock: 15, imageKey: 'detergent' },
    { id: 19, nameEn: 'Lemon Dishwash Gel', nameHi: 'नींबू डिशवॉश जेल', category: 'HOUSEHOLD', priceInr: 104, mrpInr: 118, unitEn: '500 ml', unitHi: '500 मिली', stock: 19, imageKey: 'dishwash' },
    { id: 20, nameEn: 'Neem & Tulsi Bath Soap', nameHi: 'नीम-तुलसी नहाने का साबुन', category: 'HOUSEHOLD', priceInr: 90, mrpInr: 105, unitEn: '3 x 100g', unitHi: '3 x 100ग्राम', stock: 21, imageKey: 'soap' }
  ];

  function createDefaultOrders() {
    const now = Date.now();
    return [
      {
        id: 1,
        orderCode: 'GK-1001',
        customerName: 'Priya Verma',
        customerPhone: '9811223344',
        deliveryAddress: 'House 8, Mandir Marg, Rampur Block, Jaipur',
        paymentMethod: 'Cash on Delivery (Demo)',
        subtotalInr: 340,
        deliveryFeeInr: 0,
        totalInr: 340,
        status: 'DELIVERED',
        createdAtMillis: now - 1000 * 60 * 60 * 18,
        notes: 'Ring bell twice',
        items: [
          { productId: 1, nameEn: 'Royal Basmati Rice', nameHi: 'रॉयल बासमती चावल', category: 'GROCERY', unitPriceInr: 128, quantity: 1, unitEn: '1 kg', imageKey: 'rice' },
          { productId: 5, nameEn: 'Fresh Desi Tomatoes', nameHi: 'ताज़ा देसी टमाटर', category: 'VEGETABLES', unitPriceInr: 38, quantity: 2, unitEn: '1 kg', imageKey: 'tomato' },
          { productId: 12, nameEn: 'Farm Fresh Toned Milk', nameHi: 'ताज़ा टोंड दूध', category: 'DAIRY', unitPriceInr: 54, quantity: 2, unitEn: '1 L', imageKey: 'milk' },
          { productId: 15, nameEn: 'Paracetamol 500mg', nameHi: 'पैरासिटामोल 500mg', category: 'MEDICAL', unitPriceInr: 28, quantity: 1, unitEn: '15 tabs', imageKey: 'medicine' }
        ]
      },
      {
        id: 2,
        orderCode: 'GK-1002',
        customerName: 'Rohan Patidar',
        customerPhone: '9765432109',
        deliveryAddress: 'Plot 22, Kisan Colony, Near Primary School',
        paymentMethod: 'Cash on Delivery (Demo)',
        subtotalInr: 522,
        deliveryFeeInr: 0,
        totalInr: 522,
        status: 'CONFIRMED',
        createdAtMillis: now - 1000 * 60 * 45,
        notes: '',
        items: [
          { productId: 2, nameEn: 'Whole Wheat Chakki Atta', nameHi: 'चक्की फ्रेश आटा', category: 'GROCERY', unitPriceInr: 245, quantity: 1, unitEn: '5 kg', imageKey: 'atta' },
          { productId: 9, nameEn: 'Sweet Kesar Mangoes', nameHi: 'मीठे केसर आम', category: 'FRUITS', unitPriceInr: 145, quantity: 1, unitEn: '1 kg', imageKey: 'mango' },
          { productId: 18, nameEn: 'Active Wash Detergent', nameHi: 'एक्टिव वॉश डिटर्जेंट', category: 'HOUSEHOLD', unitPriceInr: 132, quantity: 1, unitEn: '1 kg', imageKey: 'detergent' }
        ]
      },
      {
        id: 3,
        orderCode: 'GK-1003',
        customerName: 'Sunita Devi',
        customerPhone: '9922334455',
        deliveryAddress: 'Ward 5, Green Park Lane, Gramin Bazar',
        paymentMethod: 'Cash on Delivery (Demo)',
        subtotalInr: 250,
        deliveryFeeInr: 25,
        totalInr: 275,
        status: 'PENDING',
        createdAtMillis: now - 1000 * 60 * 10,
        notes: 'Call before arriving',
        items: [
          { productId: 5, nameEn: 'Fresh Desi Tomatoes', nameHi: 'ताज़ा देसी टमाटर', category: 'VEGETABLES', unitPriceInr: 38, quantity: 2, unitEn: '1 kg', imageKey: 'tomato' },
          { productId: 6, nameEn: 'Organic Potatoes (Aloo)', nameHi: 'ताज़ा आलू', category: 'VEGETABLES', unitPriceInr: 32, quantity: 1, unitEn: '1 kg', imageKey: 'potato' },
          { productId: 12, nameEn: 'Farm Fresh Toned Milk', nameHi: 'ताज़ा टोंड दूध', category: 'DAIRY', unitPriceInr: 54, quantity: 1, unitEn: '1 L', imageKey: 'milk' },
          { productId: 13, nameEn: 'Soft Malai Paneer', nameHi: 'ताज़ा मलाई पनीर', category: 'DAIRY', unitPriceInr: 88, quantity: 1, unitEn: '200 g', imageKey: 'paneer' }
        ]
      }
    ];
  }

  const DEFAULT_PROFILE = {
    name: 'Aarav Sharma',
    phone: '9876543210',
    address: 'House 14, Shanti Nagar, Near Gram Panchayat, Jaipur 302006'
  };

  const TEXT = {
    EN: {
      appName: 'GramKart',
      demo: 'DEMO',
      demoStrip: 'Testing / Demo Version • localStorage only • No real payments',
      installPrompt: 'Install GramKart on your phone for offline demo access',
      installBtn: 'Install App',
      admin: 'Admin',
      searchPlaceholder: 'Search rice, tomatoes, milk, medicines...',
      heroBadge: 'FRESH & FAST DEMO',
      heroTitle: 'Daily Groceries\nDelivered Fresh',
      heroSub: 'Farm veggies, staples, dairy & essentials at fair INR prices.',
      shopNow: 'Shop Now',
      categories: 'Shop by Category',
      all: 'All',
      freshPicks: 'Fresh Picks for You',
      itemsCount: (n) => `${n} items`,
      add: 'ADD',
      outOfStock: 'Out of Stock',
      onlyLeft: (n) => `Only ${n} left`,
      noProductsFound: 'No matching products found',
      showAllProducts: 'Show All Products',
      viewCart: 'View Cart',
      navHome: 'Home',
      navCart: 'Cart',
      navOrders: 'Orders',
      navProfile: 'Profile',
      cartTitle: 'Your Cart',
      emptyCartTitle: 'Your GramKart basket is empty',
      emptyCartSub: 'Add fresh vegetables, fruits, dairy, and daily groceries to get started.',
      browseGroceries: 'Browse Groceries',
      freeUnlocked: 'Yay! You unlocked FREE delivery',
      addMoreFree: (n) => `Add ₹${n} more for FREE delivery`,
      billSummary: 'Bill Summary',
      subtotal: 'Item Subtotal',
      deliveryFee: 'Delivery Fee',
      free: 'FREE',
      grandTotal: 'Grand Total',
      proceedCheckout: 'Proceed to Checkout',
      checkoutTitle: 'Checkout (Demo)',
      deliveryDetails: 'Delivery Details',
      fullName: 'Customer Name',
      phone: 'Mobile Number (10 digits)',
      address: 'Delivery Address',
      notes: 'Delivery Instructions (Optional)',
      codTitle: 'Cash on Delivery (Demo)',
      codSub: 'Pay cash on delivery. No payment gateway used.',
      placeOrder: (t) => `Place Demo Order • ₹${t}`,
      orderPlacedTitle: 'Order Placed!',
      orderPlacedSub: (c) => `Demo Order ${c} confirmed. Track live status updates in My Orders.`,
      trackOrder: 'Track My Order',
      continueShopping: 'Continue Shopping',
      myOrders: 'My Orders',
      noOrders: 'No orders yet',
      deliverTo: 'Deliver to',
      profileTitle: 'Customer Profile',
      saveDetails: 'Save Details',
      savedMsg: 'Customer details saved to localStorage',
      language: 'App Language / भाषा',
      openAdmin: 'Open Admin Panel',
      adminLoginTitle: 'GramKart Admin',
      adminLoginSub: 'Sign in with demo credentials to manage products & orders.',
      fillCreds: 'Fill Demo Credentials',
      signIn: 'Sign In to Dashboard',
      backStore: 'Back to Customer App',
      invalidCreds: 'Invalid credentials. Use admin@gramkart.demo / Admin123',
      dashNav: 'Dashboard',
      prodNav: 'Products',
      ordNav: 'Orders',
      setNav: 'Settings',
      totalSales: 'Total Sales',
      totalOrders: 'Total Orders',
      pendingOrders: 'Pending Orders',
      lowStock: 'Low Stock',
      salesByCat: 'Sales by Category',
      pendingQueue: 'Needs Action (Pending)',
      noPending: 'All caught up! No pending orders.',
      addProduct: '+ Add Product',
      editProduct: 'Edit Product',
      saveProduct: 'Save Product',
      cancel: 'Cancel',
      delete: 'Delete',
      stock: 'Stock',
      accept: 'Accept',
      reject: 'Reject',
      startPreparing: 'Start Preparing',
      outForDelivery: 'Out for Delivery',
      markDelivered: 'Mark Delivered',
      viewDetails: 'View Details',
      orderDetails: 'Order Details',
      updateStatus: 'Update Status',
      close: 'Close',
      searchOrders: 'Search by Order ID, customer or phone...',
      resetDemo: 'Reset Demo Data',
      resetDesc: 'Restore default sample products, ₹ prices, stock, and sample orders.',
      resetDone: 'Demo data restored to defaults',
      viewCustomer: 'View Customer Store',
      logout: 'Logout Admin'
    },
    HI: {
      appName: 'ग्रामकार्ट',
      demo: 'डेमो',
      demoStrip: 'टेस्टिंग / डेमो वर्ज़न • केवल localStorage • कोई असली पेमेंट नहीं',
      installPrompt: 'ऑफ़लाइन डेमो के लिए ग्रामकार्ट को फ़ोन पर इंस्टॉल करें',
      installBtn: 'इंस्टॉल करें',
      admin: 'एडमिन',
      searchPlaceholder: 'चावल, टमाटर, दूध, दवाई खोजें...',
      heroBadge: 'ताज़ा और तेज़ डेमो',
      heroTitle: 'रोज़मर्रा का किराना\nसीधे आपके द्वार',
      heroSub: 'ताज़ी सब्ज़ियाँ, फल, डेयरी और घरेलू सामान सही दामों में।',
      shopNow: 'अभी खरीदें',
      categories: 'श्रेणियाँ चुनें',
      all: 'सभी',
      freshPicks: 'आपके लिए ताज़ा उत्पाद',
      itemsCount: (n) => `${n} आइटम`,
      add: 'जोड़ें',
      outOfStock: 'स्टॉक खत्म',
      onlyLeft: (n) => `केवल ${n} बचे`,
      noProductsFound: 'कोई उत्पाद नहीं मिला',
      showAllProducts: 'सभी उत्पाद दिखाएं',
      viewCart: 'कार्ट देखें',
      navHome: 'होम',
      navCart: 'कार्ट',
      navOrders: 'ऑर्डर',
      navProfile: 'प्रोफ़ाइल',
      cartTitle: 'आपका कार्ट',
      emptyCartTitle: 'आपकी ग्रामकार्ट टोकरी खाली है',
      emptyCartSub: 'शुरू करने के लिए ताज़ी सब्ज़ियाँ, फल, दूध और किराना जोड़ें।',
      browseGroceries: 'सामान देखें',
      freeUnlocked: 'बधाई हो! मुफ़्त डिलीवरी अनलॉक हुई',
      addMoreFree: (n) => `मुफ़्त डिलीवरी के लिए ₹${n} और जोड़ें`,
      billSummary: 'बिल का विवरण',
      subtotal: 'सामान का कुल मूल्य',
      deliveryFee: 'डिलीवरी शुल्क',
      free: 'मुफ़्त',
      grandTotal: 'कुल देय राशि',
      proceedCheckout: 'चेकआउट करें',
      checkoutTitle: 'चेकआउट (डेमो)',
      deliveryDetails: 'डिलीवरी विवरण',
      fullName: 'ग्राहक का नाम',
      phone: 'मोबाइल नंबर (10 अंक)',
      address: 'डिलीवरी का पता',
      notes: 'डिलीवरी निर्देश (वैकल्पिक)',
      codTitle: 'कैश ऑन डिलीवरी (डेमो)',
      codSub: 'डिलीवरी पर नकद भुगतान। कोई ऑनलाइन गेटवे नहीं।',
      placeOrder: (t) => `डेमो ऑर्डर करें • ₹${t}`,
      orderPlacedTitle: 'ऑर्डर सफल रहा!',
      orderPlacedSub: (c) => `डेमो ऑर्डर ${c} कन्फर्म हो गया। 'ऑर्डर' में लाइव स्टेटस देखें।`,
      trackOrder: 'ऑर्डर ट्रैक करें',
      continueShopping: 'खरीदारी जारी रखें',
      myOrders: 'मेरे ऑर्डर',
      noOrders: 'अभी तक कोई ऑर्डर नहीं',
      deliverTo: 'पता',
      profileTitle: 'ग्राहक प्रोफ़ाइल',
      saveDetails: 'जानकारी सहेजें',
      savedMsg: 'जानकारी localStorage में सहेजी गई',
      language: 'ऐप भाषा / Language',
      openAdmin: 'एडमिन पैनल खोलें',
      adminLoginTitle: 'ग्रामकार्ट एडमिन',
      adminLoginSub: 'उत्पाद और ऑर्डर प्रबंधित करने के लिए डेमो लॉगिन करें।',
      fillCreds: 'डेमो क्रेडेंशियल भरें',
      signIn: 'डैशबोर्ड में लॉगिन करें',
      backStore: 'ग्राहक स्टोर पर लौटें',
      invalidCreds: 'गलत ईमेल या पासवर्ड। admin@gramkart.demo / Admin123 उपयोग करें',
      dashNav: 'डैशबोर्ड',
      prodNav: 'उत्पाद',
      ordNav: 'ऑर्डर',
      setNav: 'सेटिंग्स',
      totalSales: 'कुल बिक्री',
      totalOrders: 'कुल ऑर्डर',
      pendingOrders: 'लंबित ऑर्डर',
      lowStock: 'कम स्टॉक',
      salesByCat: 'श्रेणी अनुसार बिक्री',
      pendingQueue: 'तुरंत कार्रवाई (लंबित)',
      noPending: 'बढ़िया! कोई लंबित ऑर्डर नहीं है।',
      addProduct: '+ नया उत्पाद',
      editProduct: 'उत्पाद संपादित करें',
      saveProduct: 'उत्पाद सहेजें',
      cancel: 'रद्द करें',
      delete: 'हटाएं',
      stock: 'स्टॉक',
      accept: 'स्वीकारें',
      reject: 'अस्वीकारें',
      startPreparing: 'तैयारी शुरू करें',
      outForDelivery: 'डिलीवरी के लिए भेजें',
      markDelivered: 'डिलीवर चिह्नित करें',
      viewDetails: 'पूरा विवरण',
      orderDetails: 'ऑर्डर का पूरा विवरण',
      updateStatus: 'स्टेटस बदलें',
      close: 'बंद करें',
      searchOrders: 'ऑर्डर ID, नाम या नंबर खोजें...',
      resetDemo: 'डेमो डेटा रीसेट करें',
      resetDesc: 'डिफ़ॉल्ट उत्पाद, ₹ कीमतें, स्टॉक और सैंपल ऑर्डर वापस लाएं।',
      resetDone: 'डेमो डेटा डिफ़ॉल्ट पर रीसेट हो गया',
      viewCustomer: 'ग्राहक स्टोर देखें',
      logout: 'एडमिन लॉगआउट'
    }
  };

  function readStorage(key, fallback) {
    try {
      const raw = localStorage.getItem(key);
      return raw ? JSON.parse(raw) : fallback;
    } catch {
      return fallback;
    }
  }

  function writeStorage(key, val) {
    localStorage.setItem(key, JSON.stringify(val));
  }

  function ensureSeeded() {
    if (!localStorage.getItem(STORAGE_KEYS.PRODUCTS)) {
      writeStorage(STORAGE_KEYS.PRODUCTS, DEFAULT_PRODUCTS);
    }
    if (!localStorage.getItem(STORAGE_KEYS.ORDERS)) {
      writeStorage(STORAGE_KEYS.ORDERS, createDefaultOrders());
    }
    if (!localStorage.getItem(STORAGE_KEYS.CART)) {
      writeStorage(STORAGE_KEYS.CART, {});
    }
    if (!localStorage.getItem(STORAGE_KEYS.PROFILE)) {
      writeStorage(STORAGE_KEYS.PROFILE, DEFAULT_PROFILE);
    }
    if (!localStorage.getItem(STORAGE_KEYS.LANG)) {
      writeStorage(STORAGE_KEYS.LANG, 'EN');
    }
  }

  ensureSeeded();

  const state = {
    mode: 'CUSTOMER', // CUSTOMER | ADMIN_LOGIN | ADMIN_PANEL
    customerTab: 'HOME', // HOME | CART | ORDERS | PROFILE
    subScreen: null, // null | 'CHECKOUT' | { type: 'SUCCESS', code }
    adminTab: 'DASHBOARD', // DASHBOARD | PRODUCTS | ORDERS | SETTINGS
    lang: readStorage(STORAGE_KEYS.LANG, 'EN'),
    search: '',
    category: null,
    adminProdSearch: '',
    adminProdCat: null,
    adminOrderSearch: '',
    adminOrderFilter: null,
    installPromptEvent: null,
    installDismissed: false,
    loginError: ''
  };

  function esc(str) {
    return String(str ?? '')
      .replace(/&/g, '&amp;')
      .replace(/</g, '&lt;')
      .replace(/>/g, '&gt;')
      .replace(/"/g, '&quot;')
      .replace(/'/g, '&#39;');
  }

  function t() {
    return TEXT[state.lang] || TEXT.EN;
  }

  function prodName(p) {
    return state.lang === 'HI' && p.nameHi ? p.nameHi : p.nameEn;
  }

  function prodUnit(p) {
    return state.lang === 'HI' && p.unitHi ? p.unitHi : p.unitEn;
  }

  function catObj(id) {
    return CATEGORIES.find((c) => c.id === id) || CATEGORIES[0];
  }

  function statusObj(id) {
    const mapped = id === 'ACCEPTED' ? 'CONFIRMED' : id === 'REJECTED' ? 'CANCELLED' : id;
    return ORDER_STATUSES.find((s) => s.id === mapped) || ORDER_STATUSES[0];
  }

  function getProducts() {
    return readStorage(STORAGE_KEYS.PRODUCTS, DEFAULT_PRODUCTS);
  }

  function getOrders() {
    return readStorage(STORAGE_KEYS.ORDERS, []);
  }

  function getCart() {
    return readStorage(STORAGE_KEYS.CART, {});
  }

  function getProfile() {
    return readStorage(STORAGE_KEYS.PROFILE, DEFAULT_PROFILE);
  }

  function getCartSummary() {
    const products = getProducts();
    const cart = getCart();
    const items = [];
    let totalCount = 0;
    let subtotalInr = 0;

    for (const p of products) {
      const qty = Number(cart[p.id] || 0);
      if (qty > 0 && p.stock > 0) {
        const clamped = Math.min(qty, p.stock);
        items.push({ product: p, quantity: clamped, lineTotal: clamped * p.priceInr });
        totalCount += clamped;
        subtotalInr += clamped * p.priceInr;
      }
    }
    const deliveryFeeInr = subtotalInr === 0 || subtotalInr >= 299 ? 0 : 25;
    return {
      items,
      totalCount,
      subtotalInr,
      deliveryFeeInr,
      grandTotalInr: subtotalInr + deliveryFeeInr,
      remainingForFree: Math.max(0, 299 - subtotalInr)
    };
  }

  function showToast(msg) {
    const root = document.getElementById('toast-root');
    root.innerHTML = `<div class="toast">${esc(msg)}</div>`;
    setTimeout(() => {
      root.innerHTML = '';
    }, 2400);
  }

  function addToCart(productId) {
    const products = getProducts();
    const prod = products.find((p) => p.id === productId);
    if (!prod || prod.stock <= 0) return;
    const cart = getCart();
    const cur = Number(cart[productId] || 0);
    if (cur < prod.stock) {
      cart[productId] = cur + 1;
      writeStorage(STORAGE_KEYS.CART, cart);
      render();
    }
  }

  function decCart(productId) {
    const cart = getCart();
    const cur = Number(cart[productId] || 0);
    if (cur <= 1) delete cart[productId];
    else cart[productId] = cur - 1;
    writeStorage(STORAGE_KEYS.CART, cart);
    render();
  }

  function removeCart(productId) {
    const cart = getCart();
    delete cart[productId];
    writeStorage(STORAGE_KEYS.CART, cart);
    render();
  }

  function updateOrderStatus(orderId, newStatus) {
    const orders = getOrders();
    const products = getProducts();
    const order = orders.find((o) => o.id === orderId);
    if (!order) return;
    const oldStatus = statusObj(order.status).id;
    if (oldStatus === newStatus) return;

    if (oldStatus !== 'CANCELLED' && newStatus === 'CANCELLED') {
      order.items.forEach((item) => {
        const p = products.find((x) => x.id === item.productId);
        if (p) p.stock += item.quantity;
      });
    } else if (oldStatus === 'CANCELLED' && newStatus !== 'CANCELLED') {
      order.items.forEach((item) => {
        const p = products.find((x) => x.id === item.productId);
        if (p) p.stock = Math.max(0, p.stock - item.quantity);
      });
    }

    order.status = newStatus;
    writeStorage(STORAGE_KEYS.PRODUCTS, products);
    writeStorage(STORAGE_KEYS.ORDERS, orders);
    render();
  }

  function renderOrderStepper(statusId) {
    const st = statusObj(statusId);
    if (st.id === 'CANCELLED') {
      return `<div class="status-chip status-CANCELLED" style="width:100%;justify-content:center;padding:8px;">${esc(state.lang === 'HI' ? st.hi : st.en)}</div>`;
    }
    const steps = ORDER_STATUSES.slice(0, 5);
    const activeIdx = Math.max(0, steps.findIndex((s) => s.id === st.id));
    return `
      <div class="order-stepper">
        ${steps.map((s, idx) => `
          <div>
            <div class="step-dot ${idx <= activeIdx ? 'done' : ''}"></div>
            <div class="step-label ${idx === activeIdx ? 'active' : ''}">${esc(state.lang === 'HI' ? s.hi : s.en)}</div>
          </div>
        `).join('')}
      </div>
    `;
  }

  function formatTime(ms) {
    return new Date(ms).toLocaleString('en-IN', {
      day: '2-digit',
      month: 'short',
      hour: '2-digit',
      minute: '2-digit'
    });
  }

  function getFilteredCustomerProducts() {
    const q = state.search.trim().toLowerCase();
    return getProducts().filter((p) => {
      const matchCat = !state.category || p.category === state.category;
      const matchQ = !q || p.nameEn.toLowerCase().includes(q) || p.nameHi.toLowerCase().includes(q);
      return matchCat && matchQ;
    });
  }

  function renderCustomerProductsSection() {
    const s = t();
    const cart = getCart();
    const q = state.search.trim();
    const filtered = getFilteredCustomerProducts();

    return `
      ${!q && !state.category ? `
        <div class="hero-banner">
          <img src="./assets/hero.jpg" alt="Fresh groceries" />
          <div class="hero-overlay">
            <span class="hero-badge">${esc(s.heroBadge)}</span>
            <div>
              <h2 class="hero-title">${esc(s.heroTitle).replace(/\n/g, '<br/>')}</h2>
              <p class="hero-sub">${esc(s.heroSub)}</p>
            </div>
            <button class="hero-cta" data-action="scroll-products">${esc(s.shopNow)}</button>
          </div>
        </div>
      ` : ''}

      <div class="section-header"><h3 class="section-title">${esc(s.categories)}</h3></div>
      <div class="chips-row">
        <button class="chip ${!state.category ? 'active' : ''}" data-cat="">🛍️ ${esc(s.all)}</button>
        ${CATEGORIES.map((c) => `
          <button class="chip ${state.category === c.id ? 'active' : ''}" data-cat="${c.id}">
            ${c.emoji} ${esc(state.lang === 'HI' ? c.hi : c.en)}
          </button>
        `).join('')}
      </div>

      <div id="products-anchor" class="section-header">
        <h3 class="section-title">${esc(state.category ? (state.lang === 'HI' ? catObj(state.category).hi : catObj(state.category).en) : s.freshPicks)}</h3>
        <span class="section-meta">${esc(s.itemsCount(filtered.length))}</span>
      </div>

      ${filtered.length === 0 ? `
        <div class="card empty-state">
          <div style="font-size:2rem;">🔍</div>
          <strong>${esc(s.noProductsFound)}</strong>
          <button class="btn btn-sm btn-outline" data-action="clear-cust-filters">${esc(s.showAllProducts)}</button>
        </div>
      ` : `
        <div class="product-grid">
          ${filtered.map((p) => {
            const cat = catObj(p.category);
            const qty = Number(cart[p.id] || 0);
            const disc = p.mrpInr > p.priceInr ? Math.round(((p.mrpInr - p.priceInr) * 100) / p.mrpInr) : 0;
            return `
              <div class="product-card">
                <div class="product-art" style="background:radial-gradient(circle, #ffffff 25%, ${cat.bg} 100%);">
                  <span>${ART_EMOJIS[p.imageKey] || cat.emoji}</span>
                  ${disc > 0 && p.stock > 0 ? `<span class="discount-tag">${disc}% OFF</span>` : ''}
                  ${p.stock <= 0 ? `<span class="stock-tag stock-out">${esc(s.outOfStock)}</span>` : p.stock <= 5 ? `<span class="stock-tag stock-low">${esc(s.onlyLeft(p.stock))}</span>` : ''}
                </div>
                <div>
                  <h4 class="product-name">${esc(prodName(p))}</h4>
                  <div class="product-unit">${esc(prodUnit(p))}</div>
                </div>
                <div class="product-footer">
                  <div>
                    <span class="price-now">₹${p.priceInr}</span>
                    ${p.mrpInr > p.priceInr ? `<span class="price-mrp">₹${p.mrpInr}</span>` : ''}
                  </div>
                  ${p.stock <= 0 ? `<span class="pill-badge" style="background:#FEE2E2;color:#DC2626;">${esc(s.outOfStock)}</span>`
                    : qty > 0 ? `
                      <div class="stepper">
                        <button data-dec="${p.id}">−</button>
                        <span>${qty}</span>
                        <button data-inc="${p.id}" ${qty >= p.stock ? 'disabled style="opacity:0.4"' : ''}>+</button>
                      </div>
                    ` : `<button class="btn-add" data-add="${p.id}">${esc(s.add)}</button>`}
                </div>
              </div>
            `;
          }).join('')}
        </div>
      `}
    `;
  }

  function renderCustomerHome() {
    const s = t();
    const summary = getCartSummary();

    return `
      <div class="main-scroll">
        <div class="top-header">
          <div class="brand-row">
            <img src="./icons/icon-192.png" class="brand-icon" alt="GramKart" />
            <div>
              <h1 class="brand-title">${esc(s.appName)} <span class="pill-badge pill-orange">${esc(s.demo)}</span></h1>
              <div class="brand-sub">₹ INR • Local PWA Demo</div>
            </div>
          </div>
          <div class="header-actions">
            <button class="pill-btn green" data-action="toggle-lang">${state.lang === 'EN' ? '<strong>EN</strong> / हिं' : 'EN / <strong>हिं</strong>'}</button>
            <button class="pill-btn" data-action="open-admin">${esc(s.admin)}</button>
          </div>
        </div>

        <div class="demo-strip"><span class="demo-dot"></span><span>${esc(s.demoStrip)}</span></div>

        ${state.installPromptEvent && !state.installDismissed ? `
          <div class="install-banner">
            <span>${esc(s.installPrompt)}</span>
            <button data-action="install-pwa">${esc(s.installBtn)}</button>
          </div>
        ` : ''}

        <div class="sticky-search-wrap">
          <div class="search-box">
            <span class="search-icon">🔍</span>
            <input id="cust-search" type="search" class="search-input" placeholder="${esc(s.searchPlaceholder)}" value="${esc(state.search)}" aria-label="${esc(s.searchPlaceholder)}" />
            <button type="button" id="cust-search-clear" class="search-clear-btn" style="display:${state.search ? 'flex' : 'none'};" aria-label="Clear search">✕</button>
          </div>
        </div>

        <div id="customer-products-section" style="display:flex;flex-direction:column;gap:12px;">
          ${renderCustomerProductsSection()}
        </div>
      </div>

      ${summary.totalCount > 0 ? `
        <div class="floating-cart" data-nav="CART">
          <div>
            <div style="font-size:0.75rem;opacity:0.9;">${esc(s.itemsCount(summary.totalCount))}</div>
            <div style="font-size:1.05rem;font-weight:800;">₹${summary.grandTotalInr}</div>
          </div>
          <div class="floating-cart-cta">${esc(s.viewCart)} →</div>
        </div>
      ` : ''}
    `;
  }

  function renderCustomerCart() {
    const s = t();
    const summary = getCartSummary();

    if (summary.items.length === 0) {
      return `
        <div class="main-scroll">
          <h2 class="section-title" style="font-size:1.25rem;color:var(--gk-green-dark);">${esc(s.cartTitle)}</h2>
          <div class="empty-state card">
            <img src="./assets/empty-cart.jpg" alt="Empty basket" />
            <h3 style="margin:0;">${esc(s.emptyCartTitle)}</h3>
            <p style="margin:0;color:var(--gk-text-sec);font-size:0.86rem;">${esc(s.emptyCartSub)}</p>
            <button class="btn btn-primary" data-nav="HOME">${esc(s.browseGroceries)}</button>
          </div>
        </div>
      `;
    }

    const pct = Math.min(100, Math.round((summary.subtotalInr / 299) * 100));
    return `
      <div class="main-scroll">
        <div class="row-between">
          <h2 class="section-title" style="font-size:1.25rem;color:var(--gk-green-dark);">${esc(s.cartTitle)}</h2>
          <span class="pill-badge pill-green">${esc(s.itemsCount(summary.totalCount))}</span>
        </div>

        <div class="card" style="background:${summary.remainingForFree === 0 ? 'var(--gk-green-tint)' : 'var(--gk-orange-tint)'};border:none;">
          <div style="font-size:0.84rem;font-weight:700;color:${summary.remainingForFree === 0 ? 'var(--gk-green-dark)' : 'var(--gk-orange-dark)'};">
            🚚 ${esc(summary.remainingForFree === 0 ? s.freeUnlocked : s.addMoreFree(summary.remainingForFree))}
          </div>
          <div class="bar-track" style="background:#fff;">
            <div class="bar-fill" style="width:${pct}%;background:${summary.remainingForFree === 0 ? 'var(--gk-green)' : 'var(--gk-orange)'};"></div>
          </div>
        </div>

        ${summary.items.map(({ product, quantity, lineTotal }) => {
          const cat = catObj(product.category);
          return `
            <div class="card">
              <div class="row-between">
                <div class="row-gap" style="flex:1;">
                  <div style="width:52px;height:52px;border-radius:12px;background:${cat.bg};display:flex;align-items:center;justify-content:center;font-size:1.6rem;">
                    ${ART_EMOJIS[product.imageKey] || cat.emoji}
                  </div>
                  <div>
                    <div style="font-weight:700;font-size:0.9rem;">${esc(prodName(product))}</div>
                    <div style="font-size:0.76rem;color:var(--gk-text-sec);">${esc(prodUnit(product))} • ₹${product.priceInr}</div>
                    <div style="font-weight:800;color:var(--gk-green-dark);">₹${lineTotal}</div>
                  </div>
                </div>
                <div style="display:flex;flex-direction:column;align-items:flex-end;gap:6px;">
                  <button class="btn btn-sm btn-danger-outline" data-remove="${product.id}">✕</button>
                  <div class="stepper">
                    <button data-dec="${product.id}">−</button>
                    <span>${quantity}</span>
                    <button data-inc="${product.id}" ${quantity >= product.stock ? 'disabled style="opacity:0.4"' : ''}>+</button>
                  </div>
                </div>
              </div>
            </div>
          `;
        }).join('')}

        <div class="card">
          <div class="section-title">${esc(s.billSummary)}</div>
          <div class="row-between"><span>${esc(s.subtotal)}</span><strong>₹${summary.subtotalInr}</strong></div>
          <div class="row-between"><span>${esc(s.deliveryFee)}</span><strong style="color:var(--gk-green);">${summary.deliveryFeeInr === 0 ? esc(s.free) : '₹' + summary.deliveryFeeInr}</strong></div>
          <div class="divider"></div>
          <div class="row-between"><strong style="font-size:1.05rem;">${esc(s.grandTotal)}</strong><strong style="font-size:1.2rem;color:var(--gk-green-dark);">₹${summary.grandTotalInr}</strong></div>
        </div>

        <button class="btn btn-primary" data-action="go-checkout">${esc(s.proceedCheckout)} • ₹${summary.grandTotalInr}</button>
      </div>
    `;
  }

  function renderCheckout() {
    const s = t();
    const summary = getCartSummary();
    const profile = getProfile();
    return `
      <div class="main-scroll">
        <div class="row-gap">
          <button class="btn btn-sm btn-outline" data-action="cancel-sub">←</button>
          <h2 class="section-title" style="font-size:1.2rem;color:var(--gk-green-dark);">${esc(s.checkoutTitle)}</h2>
        </div>

        <div class="demo-strip"><span class="demo-dot"></span><span>${esc(s.demoStrip)}</span></div>

        <form id="checkout-form" class="card">
          <div class="section-title">${esc(s.deliveryDetails)}</div>
          <input name="name" class="input-field" placeholder="${esc(s.fullName)}" value="${esc(profile.name)}" required />
          <input name="phone" class="input-field" placeholder="${esc(s.phone)}" value="${esc(profile.phone)}" pattern="\\d{10}" maxlength="10" required />
          <textarea name="address" class="textarea-field" rows="2" placeholder="${esc(s.address)}" required>${esc(profile.address)}</textarea>
          <input name="notes" class="input-field" placeholder="${esc(s.notes)}" />

          <div class="card" style="background:var(--gk-green-soft);border-color:var(--gk-green);">
            <div style="font-weight:800;color:var(--gk-green-dark);">💵 ${esc(s.codTitle)}</div>
            <div style="font-size:0.78rem;color:var(--gk-text-sec);">${esc(s.codSub)}</div>
          </div>

          <div class="divider"></div>
          <div class="row-between"><strong>${esc(s.grandTotal)}</strong><strong style="font-size:1.15rem;color:var(--gk-green-dark);">₹${summary.grandTotalInr}</strong></div>
          <button type="submit" class="btn btn-orange">${esc(s.placeOrder(summary.grandTotalInr))}</button>
        </form>
      </div>
    `;
  }

  function renderOrderSuccess(code) {
    const s = t();
    return `
      <div class="main-scroll" style="justify-content:center;">
        <div class="empty-state card">
          <img src="./assets/order-success.jpg" alt="Order placed" />
          <span class="pill-badge pill-green" style="font-size:0.88rem;padding:6px 14px;">${esc(code)}</span>
          <h2 style="margin:0;color:var(--gk-green-dark);">${esc(s.orderPlacedTitle)}</h2>
          <p style="margin:0;color:var(--gk-text-sec);font-size:0.88rem;">${esc(s.orderPlacedSub(code))}</p>
          <button class="btn btn-primary" style="width:100%;" data-action="after-order-track">${esc(s.trackOrder)}</button>
          <button class="btn btn-outline" style="width:100%;" data-action="after-order-home">${esc(s.continueShopping)}</button>
        </div>
      </div>
    `;
  }

  function renderCustomerOrders() {
    const s = t();
    const orders = getOrders();
    return `
      <div class="main-scroll">
        <div class="row-between">
          <h2 class="section-title" style="font-size:1.25rem;color:var(--gk-green-dark);">${esc(s.myOrders)}</h2>
          <span class="pill-badge pill-green">${orders.length}</span>
        </div>

        ${orders.length === 0 ? `
          <div class="card empty-state"><p>${esc(s.noOrders)}</p><button class="btn btn-primary" data-nav="HOME">${esc(s.browseGroceries)}</button></div>
        ` : orders.map((o) => {
          const st = statusObj(o.status);
          return `
            <div class="card">
              <div class="row-between">
                <div>
                  <strong style="color:var(--gk-green-dark);">${esc(o.orderCode)}</strong>
                  <div style="font-size:0.74rem;color:var(--gk-text-muted);">${esc(formatTime(o.createdAtMillis))}</div>
                </div>
                <span class="status-chip status-${st.id}">${esc(state.lang === 'HI' ? st.hi : st.en)}</span>
              </div>
              ${renderOrderStepper(o.status)}
              <div class="divider"></div>
              ${o.items.map((it) => `
                <div class="row-between" style="font-size:0.84rem;">
                  <span>${it.quantity}x ${esc(state.lang === 'HI' && it.nameHi ? it.nameHi : it.nameEn)}</span>
                  <strong>₹${it.unitPriceInr * it.quantity}</strong>
                </div>
              `).join('')}
              <div class="divider"></div>
              <div class="row-between">
                <div style="font-size:0.76rem;color:var(--gk-text-sec);max-width:72%;">
                  <strong>${esc(s.deliverTo)}:</strong> ${esc(o.customerName)} (${esc(o.customerPhone)})<br/>
                  ${esc(o.deliveryAddress)}
                </div>
                <strong style="font-size:1.1rem;color:var(--gk-green-dark);">₹${o.totalInr}</strong>
              </div>
            </div>
          `;
        }).join('')}
      </div>
    `;
  }

  function renderCustomerProfile() {
    const s = t();
    const profile = getProfile();
    return `
      <div class="main-scroll">
        <div class="card" style="background:var(--gk-green);color:#fff;border:none;">
          <div style="font-size:1.1rem;font-weight:800;">👤 ${esc(profile.name)}</div>
          <div style="font-size:0.82rem;opacity:0.9;">+91 ${esc(profile.phone)} • Demo Customer</div>
        </div>

        <div class="card">
          <div class="section-title">${esc(s.language)}</div>
          <div class="row-gap">
            <button class="chip ${state.lang === 'EN' ? 'active' : ''}" data-lang="EN">English</button>
            <button class="chip ${state.lang === 'HI' ? 'active' : ''}" data-lang="HI">हिन्दी</button>
          </div>
        </div>

        <form id="profile-form" class="card">
          <div class="section-title">${esc(s.deliveryDetails)}</div>
          <input name="name" class="input-field" value="${esc(profile.name)}" required />
          <input name="phone" class="input-field" value="${esc(profile.phone)}" maxlength="10" required />
          <textarea name="address" class="textarea-field" rows="2" required>${esc(profile.address)}</textarea>
          <button type="submit" class="btn btn-primary">${esc(s.saveDetails)}</button>
        </form>

        <div class="card" style="background:var(--gk-orange-tint);border-color:rgba(234,88,12,0.3);">
          <div class="section-title" style="color:var(--gk-orange-dark);">🛡️ ${esc(s.admin)}</div>
          <button class="btn btn-orange" data-action="open-admin">${esc(s.openAdmin)}</button>
        </div>
      </div>
    `;
  }

  function renderAdminLogin() {
    const s = t();
    return `
      <div class="main-scroll" style="justify-content:center;">
        <div class="row-between">
          <button class="pill-btn" data-action="back-customer">← ${esc(s.backStore)}</button>
          <button class="pill-btn green" data-action="toggle-lang">${state.lang === 'EN' ? 'EN / हिं' : 'हिं / EN'}</button>
        </div>

        <form id="admin-login-form" class="card" style="padding:20px;">
          <h2 style="margin:0;color:var(--gk-green-dark);text-align:center;">${esc(s.adminLoginTitle)}</h2>
          <p style="margin:0;color:var(--gk-text-sec);font-size:0.84rem;text-align:center;">${esc(s.adminLoginSub)}</p>
          <div class="row-between" style="background:var(--gk-orange-tint);padding:10px;border-radius:12px;">
            <span style="font-size:0.72rem;font-weight:700;color:var(--gk-orange-dark);">admin@gramkart.demo / Admin123</span>
            <button type="button" class="btn btn-sm btn-orange" data-action="fill-demo-creds">${esc(s.fillCreds)}</button>
          </div>
          <input id="admin-email" name="email" type="email" class="input-field" placeholder="admin@gramkart.demo" required />
          <input id="admin-pass" name="password" type="password" class="input-field" placeholder="Admin123" required />
          ${state.loginError ? `<div style="background:var(--gk-red-tint);color:var(--gk-red);padding:8px 10px;border-radius:10px;font-size:0.8rem;font-weight:600;">${esc(state.loginError)}</div>` : ''}
          <button type="submit" class="btn btn-primary">${esc(s.signIn)}</button>
        </form>
      </div>
    `;
  }

  function renderAdminDashboard() {
    const s = t();
    const orders = getOrders();
    const products = getProducts();
    const activeOrders = orders.filter((o) => statusObj(o.status).id !== 'CANCELLED');
    const totalSales = activeOrders.reduce((acc, o) => acc + o.totalInr, 0);
    const pending = orders.filter((o) => statusObj(o.status).id === 'PENDING');
    const lowStock = products.filter((p) => p.stock <= 5).length;

    const catStats = CATEGORIES.map((c) => {
      const items = activeOrders.flatMap((o) => o.items).filter((i) => i.category === c.id);
      return {
        cat: c,
        rev: items.reduce((a, i) => a + i.unitPriceInr * i.quantity, 0),
        qty: items.reduce((a, i) => a + i.quantity, 0)
      };
    }).sort((a, b) => b.rev - a.rev);
    const maxRev = Math.max(1, ...catStats.map((c) => c.rev));

    return `
      <div class="main-scroll">
        <div class="kpi-grid">
          <div class="card"><div class="kpi-val" style="color:var(--gk-green-dark);">₹${totalSales}</div><div class="kpi-lbl">${esc(s.totalSales)}</div></div>
          <div class="card"><div class="kpi-val" style="color:var(--gk-blue);">${orders.length}</div><div class="kpi-lbl">${esc(s.totalOrders)}</div></div>
          <div class="card"><div class="kpi-val" style="color:var(--gk-orange-dark);">${pending.length}</div><div class="kpi-lbl">${esc(s.pendingOrders)}</div></div>
          <div class="card"><div class="kpi-val" style="color:${lowStock > 0 ? 'var(--gk-red)' : 'var(--gk-green-dark)'};">${lowStock} / ${products.length}</div><div class="kpi-lbl">${esc(s.lowStock)}</div></div>
        </div>

        <div class="card">
          <div class="section-title">${esc(s.salesByCat)}</div>
          ${catStats.map((st) => `
            <div>
              <div class="row-between" style="font-size:0.8rem;margin-bottom:4px;">
                <strong>${esc(state.lang === 'HI' ? st.cat.hi : st.cat.en)}</strong>
                <span style="color:var(--gk-text-sec);">₹${st.rev} (${st.qty})</span>
              </div>
              <div class="bar-track" style="background:${st.cat.bg};">
                <div class="bar-fill" style="width:${st.rev > 0 ? Math.max(6, Math.round((st.rev / maxRev) * 100)) : 0}%;background:${st.cat.fg};"></div>
              </div>
            </div>
          `).join('')}
        </div>

        <div class="section-title">${esc(s.pendingQueue)}</div>
        ${pending.length === 0 ? `<div class="card" style="background:var(--gk-green-tint);color:var(--gk-green-dark);">${esc(s.noPending)}</div>` : pending.map((o) => `
          <div class="card" style="border-color:rgba(249,115,22,0.45);">
            <div class="row-between">
              <div><strong>${esc(o.orderCode)} • ${esc(o.customerName)}</strong><div style="font-size:0.76rem;color:var(--gk-text-sec);">${esc(o.customerPhone)}</div></div>
              <strong style="color:var(--gk-green-dark);">₹${o.totalInr}</strong>
            </div>
            <div class="row-gap">
              <button class="btn btn-sm btn-primary" style="flex:1;" data-order-status="${o.id}:CONFIRMED">✓ ${esc(s.accept)}</button>
              <button class="btn btn-sm btn-danger-outline" style="flex:1;" data-order-status="${o.id}:CANCELLED">✕ ${esc(s.reject)}</button>
            </div>
          </div>
        `).join('')}
      </div>
    `;
  }

  function renderAdminProductsListSection() {
    const s = t();
    const q = state.adminProdSearch.trim().toLowerCase();
    const products = getProducts().filter((p) => {
      const mc = !state.adminProdCat || p.category === state.adminProdCat;
      const mq = !q || p.nameEn.toLowerCase().includes(q) || p.nameHi.toLowerCase().includes(q);
      return mc && mq;
    });

    if (products.length === 0) {
      return `
        <div class="card empty-state">
          <div style="font-size:1.8rem;">🔍</div>
          <strong>${esc(s.noProductsFound)}</strong>
          <button class="btn btn-sm btn-outline" data-action="clear-admin-prod-filters">${esc(s.showAllProducts)}</button>
        </div>
      `;
    }

    return products.map((p) => {
      const cat = catObj(p.category);
      return `
        <div class="card">
          <div class="row-between">
            <div class="row-gap" style="flex:1;">
              <div style="width:48px;height:48px;border-radius:12px;background:${cat.bg};display:flex;align-items:center;justify-content:center;font-size:1.5rem;">
                ${ART_EMOJIS[p.imageKey] || cat.emoji}
              </div>
              <div>
                <div style="font-weight:700;font-size:0.9rem;">${esc(prodName(p))}</div>
                <div style="font-size:0.75rem;color:var(--gk-text-sec);">${esc(state.lang === 'HI' ? cat.hi : cat.en)} • ${esc(prodUnit(p))}</div>
                <div class="row-gap" style="margin-top:4px;">
                  <strong style="color:var(--gk-green-dark);">₹${p.priceInr}</strong>
                  <span class="pill-badge ${p.stock <= 5 ? 'pill-orange' : 'pill-green'}">
                    <button style="border:none;background:none;font-weight:800;cursor:pointer;" data-stock="${p.id}:-1">−</button>
                    ${esc(s.stock)}: ${p.stock}
                    <button style="border:none;background:none;font-weight:800;cursor:pointer;" data-stock="${p.id}:1">+</button>
                  </span>
                </div>
              </div>
            </div>
            <div style="display:flex;flex-direction:column;gap:4px;">
              <button class="btn btn-sm btn-outline" data-edit-prod="${p.id}">✎</button>
              <button class="btn btn-sm btn-danger-outline" data-del-prod="${p.id}">🗑</button>
            </div>
          </div>
        </div>
      `;
    }).join('');
  }

  function renderAdminProducts() {
    const s = t();

    return `
      <div class="main-scroll">
        <div class="sticky-search-wrap">
          <div class="row-between">
            <div class="search-box" style="flex:1;">
              <span class="search-icon">🔍</span>
              <input id="admin-prod-search" type="search" class="search-input" placeholder="${esc(s.searchPlaceholder)}" value="${esc(state.adminProdSearch)}" />
              <button type="button" id="admin-prod-search-clear" class="search-clear-btn" style="display:${state.adminProdSearch ? 'flex' : 'none'};" aria-label="Clear search">✕</button>
            </div>
            <button class="btn btn-sm btn-orange" data-action="add-product">${esc(s.addProduct)}</button>
          </div>
        </div>
        <div class="chips-row">
          <button class="chip ${!state.adminProdCat ? 'active' : ''}" data-admin-cat="">${esc(s.all)}</button>
          ${CATEGORIES.map((c) => `<button class="chip ${state.adminProdCat === c.id ? 'active' : ''}" data-admin-cat="${c.id}">${esc(state.lang === 'HI' ? c.hi : c.en)}</button>`).join('')}
        </div>

        <div id="admin-products-list" style="display:flex;flex-direction:column;gap:10px;">
          ${renderAdminProductsListSection()}
        </div>
      </div>
    `;
  }

  function renderAdminOrders() {
    const s = t();
    const allOrders = getOrders();
    const q = state.adminOrderSearch.trim().toLowerCase();
    const filtered = allOrders.filter((o) => {
      const st = statusObj(o.status).id;
      const ms = !state.adminOrderFilter || st === state.adminOrderFilter;
      const mq = !q || o.orderCode.toLowerCase().includes(q) || o.customerName.toLowerCase().includes(q) || o.customerPhone.includes(q);
      return ms && mq;
    });

    return `
      <div class="main-scroll">
        <input id="admin-order-search" type="search" class="search-input" placeholder="${esc(s.searchOrders)}" value="${esc(state.adminOrderSearch)}" />
        <div class="chips-row">
          <button class="chip ${!state.adminOrderFilter ? 'active' : ''}" data-ord-filter="">${esc(s.all)} (${allOrders.length})</button>
          ${ORDER_STATUSES.map((st) => {
            const cnt = allOrders.filter((o) => statusObj(o.status).id === st.id).length;
            return `<button class="chip ${state.adminOrderFilter === st.id ? 'active' : ''}" data-ord-filter="${st.id}">${esc(state.lang === 'HI' ? st.hi : st.en)} (${cnt})</button>`;
          }).join('')}
        </div>

        ${filtered.length === 0 ? `<div class="card empty-state"><p>${esc(s.noOrders)}</p></div>` : filtered.map((o) => {
          const st = statusObj(o.status);
          return `
            <div class="card">
              <div class="row-between">
                <div>
                  <span class="pill-badge pill-green">${esc(o.orderCode)}</span>
                  <span style="font-size:0.74rem;color:var(--gk-text-sec);margin-left:6px;">${esc(formatTime(o.createdAtMillis))}</span>
                </div>
                <select class="select-field" style="width:auto;padding:5px 8px;font-size:0.76rem;font-weight:700;" data-order-select="${o.id}">
                  ${ORDER_STATUSES.map((opt) => `<option value="${opt.id}" ${opt.id === st.id ? 'selected' : ''}>${esc(state.lang === 'HI' ? opt.hi : opt.en)}</option>`).join('')}
                </select>
              </div>
              <div style="background:rgba(220,252,231,0.4);padding:9px;border-radius:10px;font-size:0.8rem;">
                <strong>${esc(o.customerName)}</strong> • ${esc(o.customerPhone)}<br/>
                <span style="color:var(--gk-text-sec);">${esc(o.deliveryAddress)}</span>
              </div>
              <div class="row-between">
                <button class="btn btn-sm btn-outline" data-view-order="${o.id}">👁 ${esc(s.viewDetails)}</button>
                <strong style="font-size:1.1rem;color:var(--gk-green-dark);">₹${o.totalInr}</strong>
              </div>
              ${st.id === 'PENDING' ? `
                <div class="row-gap">
                  <button class="btn btn-sm btn-primary" style="flex:1;" data-order-status="${o.id}:CONFIRMED">✓ ${esc(s.accept)}</button>
                  <button class="btn btn-sm btn-danger-outline" style="flex:1;" data-order-status="${o.id}:CANCELLED">✕ ${esc(s.reject)}</button>
                </div>
              ` : st.id === 'CONFIRMED' ? `
                <div class="row-gap">
                  <button class="btn btn-sm btn-orange" style="flex:1;" data-order-status="${o.id}:PREPARING">${esc(s.startPreparing)}</button>
                  <button class="btn btn-sm btn-danger-outline" data-order-status="${o.id}:CANCELLED">${esc(s.reject)}</button>
                </div>
              ` : st.id === 'PREPARING' ? `
                <div class="row-gap">
                  <button class="btn btn-sm btn-purple" style="flex:1;" data-order-status="${o.id}:OUT_FOR_DELIVERY">${esc(s.outForDelivery)}</button>
                  <button class="btn btn-sm btn-danger-outline" data-order-status="${o.id}:CANCELLED">${esc(s.reject)}</button>
                </div>
              ` : st.id === 'OUT_FOR_DELIVERY' ? `
                <button class="btn btn-sm btn-primary" data-order-status="${o.id}:DELIVERED">✓ ${esc(s.markDelivered)}</button>
              ` : ''}
            </div>
          `;
        }).join('')}
      </div>
    `;
  }

  function renderAdminSettings() {
    const s = t();
    return `
      <div class="main-scroll">
        <div class="card" style="background:var(--gk-green-dark);color:#fff;border:none;">
          <strong>🛡️ GramKart Demo Admin</strong>
          <span style="font-size:0.82rem;opacity:0.9;">admin@gramkart.demo</span>
        </div>

        <div class="card">
          <div class="section-title">${esc(s.language)}</div>
          <div class="row-gap">
            <button class="chip ${state.lang === 'EN' ? 'active' : ''}" data-lang="EN">English</button>
            <button class="chip ${state.lang === 'HI' ? 'active' : ''}" data-lang="HI">हिन्दी</button>
          </div>
        </div>

        <div class="card" style="background:var(--gk-orange-tint);border-color:rgba(249,115,22,0.35);">
          <div class="section-title" style="color:var(--gk-orange-dark);">↻ ${esc(s.resetDemo)}</div>
          <div style="font-size:0.82rem;color:var(--gk-text-sec);">${esc(s.resetDesc)}</div>
          <button class="btn btn-orange" data-action="reset-demo">${esc(s.resetDemo)}</button>
        </div>

        <button class="btn btn-primary" data-action="back-customer">${esc(s.viewCustomer)}</button>
        <button class="btn btn-danger-outline" data-action="logout-admin">${esc(s.logout)}</button>
      </div>
    `;
  }

  function renderBottomNav(isAdmin, badgeCount) {
    const s = t();
    if (isAdmin) {
      const tabs = [
        { id: 'DASHBOARD', icon: '📊', label: s.dashNav, badge: 0 },
        { id: 'PRODUCTS', icon: '📦', label: s.prodNav, badge: 0 },
        { id: 'ORDERS', icon: '🧾', label: s.ordNav, badge: badgeCount },
        { id: 'SETTINGS', icon: '⚙️', label: s.setNav, badge: 0 }
      ];
      return `
        <nav class="bottom-nav">
          ${tabs.map((tb) => `
            <button class="nav-item ${state.adminTab === tb.id ? 'active' : ''}" data-admin-nav="${tb.id}">
              ${tb.badge > 0 ? `<span class="nav-badge">${tb.badge}</span>` : ''}
              <span style="font-size:1.15rem;">${tb.icon}</span>
              <span>${esc(tb.label)}</span>
            </button>
          `).join('')}
        </nav>
      `;
    }
    const tabs = [
      { id: 'HOME', icon: '🏠', label: s.navHome, badge: 0 },
      { id: 'CART', icon: '🛒', label: s.navCart, badge: badgeCount },
      { id: 'ORDERS', icon: '🧾', label: s.navOrders, badge: 0 },
      { id: 'PROFILE', icon: '👤', label: s.navProfile, badge: 0 }
    ];
    return `
      <nav class="bottom-nav">
        ${tabs.map((tb) => `
          <button class="nav-item ${state.customerTab === tb.id ? 'active' : ''}" data-nav="${tb.id}">
            ${tb.badge > 0 ? `<span class="nav-badge">${tb.badge}</span>` : ''}
            <span style="font-size:1.15rem;">${tb.icon}</span>
            <span>${esc(tb.label)}</span>
          </button>
        `).join('')}
      </nav>
    `;
  }

  function openOrderDetailsModal(orderId) {
    const order = getOrders().find((o) => o.id === orderId);
    if (!order) return;
    const s = t();
    const st = statusObj(order.status);
    const modalRoot = document.getElementById('modal-root');

    modalRoot.innerHTML = `
      <div class="modal-backdrop" id="modal-backdrop">
        <div class="modal-card">
          <div class="row-between">
            <div>
              <strong style="font-size:1.1rem;color:var(--gk-green-dark);">${esc(s.orderDetails)} • ${esc(order.orderCode)}</strong>
              <div style="font-size:0.74rem;color:var(--gk-text-sec);">${esc(formatTime(order.createdAtMillis))}</div>
            </div>
            <button class="btn btn-sm btn-outline" id="modal-close">✕</button>
          </div>
          <div class="row-gap">
            <span class="status-chip status-${st.id}">${esc(state.lang === 'HI' ? st.hi : st.en)}</span>
            <span class="pill-badge pill-green">💵 ${esc(order.paymentMethod)}</span>
          </div>
          <div style="background:var(--gk-green-soft);padding:10px;border-radius:12px;font-size:0.84rem;">
            <strong>${esc(order.customerName)}</strong> (${esc(order.customerPhone)})<br/>
            <span>${esc(order.deliveryAddress)}</span>
            ${order.notes ? `<div style="color:var(--gk-orange-dark);margin-top:4px;">📝 ${esc(order.notes)}</div>` : ''}
          </div>
          ${order.items.map((it) => `
            <div class="row-between" style="font-size:0.86rem;">
              <span>${it.quantity} × ${esc(state.lang === 'HI' && it.nameHi ? it.nameHi : it.nameEn)} (₹${it.unitPriceInr})</span>
              <strong>₹${it.unitPriceInr * it.quantity}</strong>
            </div>
          `).join('')}
          <div class="divider"></div>
          <div class="row-between"><span>${esc(s.subtotal)}</span><span>₹${order.subtotalInr}</span></div>
          <div class="row-between"><span>${esc(s.deliveryFee)}</span><span>${order.deliveryFeeInr === 0 ? esc(s.free) : '₹' + order.deliveryFeeInr}</span></div>
          <div class="row-between"><strong>${esc(s.grandTotal)}</strong><strong style="color:var(--gk-green-dark);font-size:1.1rem;">₹${order.totalInr}</strong></div>
          <div class="divider"></div>
          <div style="font-weight:700;font-size:0.84rem;">${esc(s.updateStatus)}</div>
          <div class="chips-row">
            ${ORDER_STATUSES.map((opt) => `
              <button class="chip ${opt.id === st.id ? 'active' : ''}" data-modal-status="${order.id}:${opt.id}">${esc(state.lang === 'HI' ? opt.hi : opt.en)}</button>
            `).join('')}
          </div>
          <button class="btn btn-primary" id="modal-done">${esc(s.close)}</button>
        </div>
      </div>
    `;

    modalRoot.querySelector('#modal-close').onclick = () => { modalRoot.innerHTML = ''; };
    modalRoot.querySelector('#modal-done').onclick = () => { modalRoot.innerHTML = ''; };
    modalRoot.querySelector('#modal-backdrop').onclick = (e) => {
      if (e.target.id === 'modal-backdrop') modalRoot.innerHTML = '';
    };
    modalRoot.querySelectorAll('[data-modal-status]').forEach((btn) => {
      btn.onclick = () => {
        const [oid, statusId] = btn.getAttribute('data-modal-status').split(':');
        updateOrderStatus(Number(oid), statusId);
        openOrderDetailsModal(Number(oid));
      };
    });
  }

  function openProductModal(productId) {
    const s = t();
    const products = getProducts();
    const existing = productId ? products.find((p) => p.id === productId) : null;
    const modalRoot = document.getElementById('modal-root');

    modalRoot.innerHTML = `
      <div class="modal-backdrop" id="modal-backdrop">
        <form class="modal-card" id="prod-modal-form">
          <h3 style="margin:0;color:var(--gk-green-dark);">${esc(existing ? s.editProduct : s.addProduct)}</h3>
          <input name="nameEn" class="input-field" placeholder="Product Name (English)" value="${esc(existing?.nameEn || '')}" required />
          <input name="nameHi" class="input-field" placeholder="Product Name (Hindi)" value="${esc(existing?.nameHi || '')}" />
          <select name="category" class="select-field">
            ${CATEGORIES.map((c) => `<option value="${c.id}" ${existing?.category === c.id ? 'selected' : ''}>${esc(state.lang === 'HI' ? c.hi : c.en)}</option>`).join('')}
          </select>
          <div class="row-gap">
            <input name="priceInr" type="number" min="1" class="input-field" placeholder="Price ₹" value="${existing?.priceInr || ''}" required />
            <input name="mrpInr" type="number" min="1" class="input-field" placeholder="MRP ₹" value="${existing?.mrpInr || ''}" />
          </div>
          <div class="row-gap">
            <input name="unitEn" class="input-field" placeholder="Unit (e.g. 1 kg)" value="${esc(existing?.unitEn || '1 kg')}" required />
            <input name="stock" type="number" min="0" class="input-field" placeholder="Stock" value="${existing?.stock ?? 15}" required />
          </div>
          <div class="row-gap">
            <button type="button" class="btn btn-outline" style="flex:1;" id="modal-cancel">${esc(s.cancel)}</button>
            <button type="submit" class="btn btn-primary" style="flex:1;">${esc(s.saveProduct)}</button>
          </div>
        </form>
      </div>
    `;

    modalRoot.querySelector('#modal-cancel').onclick = () => { modalRoot.innerHTML = ''; };
    modalRoot.querySelector('#modal-backdrop').onclick = (e) => {
      if (e.target.id === 'modal-backdrop') modalRoot.innerHTML = '';
    };
    modalRoot.querySelector('#prod-modal-form').onsubmit = (e) => {
      e.preventDefault();
      const fd = new FormData(e.target);
      const price = Math.max(1, Number(fd.get('priceInr')));
      const mrp = Math.max(price, Number(fd.get('mrpInr') || price));
      const cat = String(fd.get('category'));
      const entry = {
        id: existing ? existing.id : Date.now(),
        nameEn: String(fd.get('nameEn')).trim(),
        nameHi: String(fd.get('nameHi')).trim() || String(fd.get('nameEn')).trim(),
        category: cat,
        priceInr: price,
        mrpInr: mrp,
        unitEn: String(fd.get('unitEn')).trim() || '1 pc',
        unitHi: existing?.unitHi || String(fd.get('unitEn')).trim() || '1 pc',
        stock: Math.max(0, Number(fd.get('stock'))),
        imageKey: existing?.imageKey || cat.toLowerCase()
      };
      const updated = existing
        ? products.map((p) => (p.id === existing.id ? entry : p))
        : [...products, entry];
      writeStorage(STORAGE_KEYS.PRODUCTS, updated);
      modalRoot.innerHTML = '';
      render();
    };
  }

  function render() {
    const app = document.getElementById('app');
    const summary = getCartSummary();
    const s = t();

    if (state.mode === 'ADMIN_LOGIN') {
      app.innerHTML = renderAdminLogin();
    } else if (state.mode === 'ADMIN_PANEL') {
      const pendingCount = getOrders().filter((o) => statusObj(o.status).id === 'PENDING').length;
      const body = state.adminTab === 'DASHBOARD' ? renderAdminDashboard()
        : state.adminTab === 'PRODUCTS' ? renderAdminProducts()
        : state.adminTab === 'ORDERS' ? renderAdminOrders()
        : renderAdminSettings();

      app.innerHTML = `
        <div class="top-header" style="background:#fff;padding:10px 14px;border-bottom:1px solid var(--gk-border);">
          <div class="brand-row">
            <img src="./icons/icon-192.png" class="brand-icon" style="width:32px;height:32px;" alt="GramKart" />
            <strong style="color:var(--gk-green-dark);">GramKart Admin</strong>
          </div>
          <div class="header-actions">
            <button class="pill-btn green" data-action="toggle-lang">${state.lang === 'EN' ? 'EN / हिं' : 'हिं / EN'}</button>
            <button class="pill-btn" data-action="back-customer">${esc(s.navHome)}</button>
          </div>
        </div>
        ${body}
        ${renderBottomNav(true, pendingCount)}
      `;
    } else {
      if (state.subScreen === 'CHECKOUT') {
        app.innerHTML = renderCheckout();
      } else if (state.subScreen && state.subScreen.type === 'SUCCESS') {
        app.innerHTML = renderOrderSuccess(state.subScreen.code);
      } else {
        const body = state.customerTab === 'HOME' ? renderCustomerHome()
          : state.customerTab === 'CART' ? renderCustomerCart()
          : state.customerTab === 'ORDERS' ? renderCustomerOrders()
          : renderCustomerProfile();
        app.innerHTML = `${body}${renderBottomNav(false, summary.totalCount)}`;
      }
    }

    bindEvents();
  }

  function bindEvents() {
    const app = document.getElementById('app');

    const custSearch = document.getElementById('cust-search');
    const custSearchClear = document.getElementById('cust-search-clear');
    if (custSearch) {
      const updateCustomerProductList = () => {
        const container = document.getElementById('customer-products-section');
        if (container) {
          container.innerHTML = renderCustomerProductsSection();
          bindEvents();
        } else {
          render();
        }
        if (custSearchClear) {
          custSearchClear.style.display = state.search ? 'flex' : 'none';
        }
      };
      custSearch.oninput = (e) => {
        state.search = e.target.value;
        updateCustomerProductList();
      };
      if (custSearchClear) {
        custSearchClear.onclick = () => {
          state.search = '';
          custSearch.value = '';
          updateCustomerProductList();
          custSearch.focus();
        };
      }
    }

    const adminProdSearch = document.getElementById('admin-prod-search');
    const adminProdSearchClear = document.getElementById('admin-prod-search-clear');
    if (adminProdSearch) {
      const updateAdminProductList = () => {
        const container = document.getElementById('admin-products-list');
        if (container) {
          container.innerHTML = renderAdminProductsListSection();
          bindEvents();
        } else {
          render();
        }
        if (adminProdSearchClear) {
          adminProdSearchClear.style.display = state.adminProdSearch ? 'flex' : 'none';
        }
      };
      adminProdSearch.oninput = (e) => {
        state.adminProdSearch = e.target.value;
        updateAdminProductList();
      };
      if (adminProdSearchClear) {
        adminProdSearchClear.onclick = () => {
          state.adminProdSearch = '';
          adminProdSearch.value = '';
          updateAdminProductList();
          adminProdSearch.focus();
        };
      }
    }

    const adminOrderSearch = document.getElementById('admin-order-search');
    if (adminOrderSearch) {
      adminOrderSearch.oninput = (e) => {
        state.adminOrderSearch = e.target.value;
        render();
        const el = document.getElementById('admin-order-search');
        if (el) { el.focus(); el.setSelectionRange(el.value.length, el.value.length); }
      };
    }

    app.querySelectorAll('[data-nav]').forEach((el) => {
      el.onclick = () => {
        state.subScreen = null;
        state.customerTab = el.getAttribute('data-nav');
        render();
      };
    });

    app.querySelectorAll('[data-admin-nav]').forEach((el) => {
      el.onclick = () => {
        state.adminTab = el.getAttribute('data-admin-nav');
        render();
      };
    });

    app.querySelectorAll('[data-cat]').forEach((el) => {
      el.onclick = () => {
        const val = el.getAttribute('data-cat') || null;
        state.category = state.category === val ? null : val;
        render();
      };
    });

    app.querySelectorAll('[data-admin-cat]').forEach((el) => {
      el.onclick = () => {
        const val = el.getAttribute('data-admin-cat') || null;
        state.adminProdCat = state.adminProdCat === val ? null : val;
        render();
      };
    });

    app.querySelectorAll('[data-ord-filter]').forEach((el) => {
      el.onclick = () => {
        const val = el.getAttribute('data-ord-filter') || null;
        state.adminOrderFilter = state.adminOrderFilter === val ? null : val;
        render();
      };
    });

    app.querySelectorAll('[data-add]').forEach((el) => {
      el.onclick = () => addToCart(Number(el.getAttribute('data-add')));
    });
    app.querySelectorAll('[data-inc]').forEach((el) => {
      el.onclick = () => addToCart(Number(el.getAttribute('data-inc')));
    });
    app.querySelectorAll('[data-dec]').forEach((el) => {
      el.onclick = () => decCart(Number(el.getAttribute('data-dec')));
    });
    app.querySelectorAll('[data-remove]').forEach((el) => {
      el.onclick = () => removeCart(Number(el.getAttribute('data-remove')));
    });

    app.querySelectorAll('[data-lang]').forEach((el) => {
      el.onclick = () => {
        state.lang = el.getAttribute('data-lang');
        writeStorage(STORAGE_KEYS.LANG, state.lang);
        render();
      };
    });

    app.querySelectorAll('[data-stock]').forEach((el) => {
      el.onclick = () => {
        const [id, delta] = el.getAttribute('data-stock').split(':');
        const products = getProducts();
        const p = products.find((x) => x.id === Number(id));
        if (p) {
          p.stock = Math.max(0, p.stock + Number(delta));
          writeStorage(STORAGE_KEYS.PRODUCTS, products);
          render();
        }
      };
    });

    app.querySelectorAll('[data-edit-prod]').forEach((el) => {
      el.onclick = () => openProductModal(Number(el.getAttribute('data-edit-prod')));
    });

    app.querySelectorAll('[data-del-prod]').forEach((el) => {
      el.onclick = () => {
        const id = Number(el.getAttribute('data-del-prod'));
        writeStorage(STORAGE_KEYS.PRODUCTS, getProducts().filter((p) => p.id !== id));
        const cart = getCart();
        delete cart[id];
        writeStorage(STORAGE_KEYS.CART, cart);
        render();
      };
    });

    app.querySelectorAll('[data-order-status]').forEach((el) => {
      el.onclick = () => {
        const [oid, st] = el.getAttribute('data-order-status').split(':');
        updateOrderStatus(Number(oid), st);
      };
    });

    app.querySelectorAll('[data-order-select]').forEach((el) => {
      el.onchange = (e) => updateOrderStatus(Number(el.getAttribute('data-order-select')), e.target.value);
    });

    app.querySelectorAll('[data-view-order]').forEach((el) => {
      el.onclick = () => openOrderDetailsModal(Number(el.getAttribute('data-view-order')));
    });

    app.querySelectorAll('[data-action]').forEach((el) => {
      el.onclick = async () => {
        const act = el.getAttribute('data-action');
        if (act === 'toggle-lang') {
          state.lang = state.lang === 'EN' ? 'HI' : 'EN';
          writeStorage(STORAGE_KEYS.LANG, state.lang);
          render();
        } else if (act === 'open-admin') {
          const isAuth = readStorage(STORAGE_KEYS.ADMIN_AUTH, false);
          state.mode = isAuth ? 'ADMIN_PANEL' : 'ADMIN_LOGIN';
          state.loginError = '';
          render();
        } else if (act === 'back-customer') {
          state.mode = 'CUSTOMER';
          render();
        } else if (act === 'scroll-products') {
          document.getElementById('products-anchor')?.scrollIntoView({ behavior: 'smooth' });
        } else if (act === 'go-checkout') {
          state.subScreen = 'CHECKOUT';
          render();
        } else if (act === 'cancel-sub') {
          state.subScreen = null;
          render();
        } else if (act === 'after-order-track') {
          state.subScreen = null;
          state.customerTab = 'ORDERS';
          render();
        } else if (act === 'after-order-home') {
          state.subScreen = null;
          state.customerTab = 'HOME';
          render();
        } else if (act === 'fill-demo-creds') {
          document.getElementById('admin-email').value = 'admin@gramkart.demo';
          document.getElementById('admin-pass').value = 'Admin123';
        } else if (act === 'clear-cust-filters') {
          state.search = '';
          state.category = null;
          render();
        } else if (act === 'clear-admin-prod-filters') {
          state.adminProdSearch = '';
          state.adminProdCat = null;
          render();
        } else if (act === 'add-product') {
          openProductModal(null);
        } else if (act === 'reset-demo') {
          writeStorage(STORAGE_KEYS.PRODUCTS, DEFAULT_PRODUCTS);
          writeStorage(STORAGE_KEYS.ORDERS, createDefaultOrders());
          writeStorage(STORAGE_KEYS.CART, {});
          writeStorage(STORAGE_KEYS.PROFILE, DEFAULT_PROFILE);
          showToast(t().resetDone);
          render();
        } else if (act === 'logout-admin') {
          writeStorage(STORAGE_KEYS.ADMIN_AUTH, false);
          state.mode = 'CUSTOMER';
          render();
        } else if (act === 'install-pwa' && state.installPromptEvent) {
          state.installPromptEvent.prompt();
          await state.installPromptEvent.userChoice;
          state.installDismissed = true;
          state.installPromptEvent = null;
          render();
        }
      };
    });

    const checkoutForm = document.getElementById('checkout-form');
    if (checkoutForm) {
      checkoutForm.onsubmit = (e) => {
        e.preventDefault();
        const fd = new FormData(checkoutForm);
        const name = String(fd.get('name')).trim();
        const phone = String(fd.get('phone')).trim();
        const address = String(fd.get('address')).trim();
        const notes = String(fd.get('notes')).trim();
        const summary = getCartSummary();
        if (summary.items.length === 0) return;

        const orders = getOrders();
        const products = getProducts();
        const maxNum = orders.reduce((max, o) => {
          const n = Number(String(o.orderCode).replace('GK-', ''));
          return Number.isFinite(n) && n > max ? n : max;
        }, 1000);
        const orderCode = `GK-${Math.max(1001, maxNum + 1)}`;

        const newOrder = {
          id: Date.now(),
          orderCode,
          customerName: name,
          customerPhone: phone,
          deliveryAddress: address,
          paymentMethod: 'Cash on Delivery (Demo)',
          subtotalInr: summary.subtotalInr,
          deliveryFeeInr: summary.deliveryFeeInr,
          totalInr: summary.grandTotalInr,
          status: 'PENDING',
          createdAtMillis: Date.now(),
          notes,
          items: summary.items.map(({ product, quantity }) => ({
            productId: product.id,
            nameEn: product.nameEn,
            nameHi: product.nameHi,
            category: product.category,
            unitPriceInr: product.priceInr,
            quantity,
            unitEn: product.unitEn,
            imageKey: product.imageKey
          }))
        };

        summary.items.forEach(({ product, quantity }) => {
          const p = products.find((x) => x.id === product.id);
          if (p) p.stock = Math.max(0, p.stock - quantity);
        });

        writeStorage(STORAGE_KEYS.PRODUCTS, products);
        writeStorage(STORAGE_KEYS.ORDERS, [newOrder, ...orders]);
        writeStorage(STORAGE_KEYS.CART, {});
        writeStorage(STORAGE_KEYS.PROFILE, { name, phone, address });
        state.subScreen = { type: 'SUCCESS', code: orderCode };
        render();
      };
    }

    const profileForm = document.getElementById('profile-form');
    if (profileForm) {
      profileForm.onsubmit = (e) => {
        e.preventDefault();
        const fd = new FormData(profileForm);
        writeStorage(STORAGE_KEYS.PROFILE, {
          name: String(fd.get('name')).trim(),
          phone: String(fd.get('phone')).trim(),
          address: String(fd.get('address')).trim()
        });
        showToast(t().savedMsg);
        render();
      };
    }

    const loginForm = document.getElementById('admin-login-form');
    if (loginForm) {
      loginForm.onsubmit = (e) => {
        e.preventDefault();
        const fd = new FormData(loginForm);
        const email = String(fd.get('email')).trim().toLowerCase();
        const pass = String(fd.get('password'));
        if (email === 'admin@gramkart.demo' && pass === 'Admin123') {
          writeStorage(STORAGE_KEYS.ADMIN_AUTH, true);
          state.mode = 'ADMIN_PANEL';
          state.adminTab = 'DASHBOARD';
          state.loginError = '';
        } else {
          state.loginError = t().invalidCreds;
        }
        render();
      };
    }
  }

  // Register Service Worker over HTTPS / localhost with subdirectory-safe scope
  if ('serviceWorker' in navigator && window.isSecureContext) {
    window.addEventListener('load', () => {
      navigator.serviceWorker
        .register('./service-worker.js', { scope: './' })
        .catch(() => {});
    });
  }

  window.addEventListener('beforeinstallprompt', (e) => {
    e.preventDefault();
    state.installPromptEvent = e;
    render();
  });

  window.addEventListener('DOMContentLoaded', () => {
    render();
    setTimeout(() => {
      document.getElementById('splash-screen')?.classList.add('hidden');
    }, 650);
  });
})();
