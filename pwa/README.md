# GramKart Progressive Web App (PWA) — GitHub Pages Setup

This `pwa/` folder contains the complete, self-contained **GramKart** demo grocery delivery PWA (Customer App + Admin Panel) backed strictly by `localStorage`—with no Firebase, external databases, payment gateways, or WhatsApp integrations.

## Features Included
- **Customer App**: Green/white/orange mobile-first UI, English & Hindi (`EN / हिं`) toggle, sticky real-time product search, 6 categories (`Grocery`, `Vegetables`, `Fruits`, `Dairy`, `Medical`, `Household`), INR prices, cart quantity controls, automatic totals, Cash on Delivery (Demo) checkout, live order tracking (`My Orders`), and Customer Profile.
- **Admin Panel** (`admin@gramkart.demo` / `Admin123`): Sales & KPI dashboard, product CRUD + stock/price updates, full Order Management (`Pending`, `Confirmed`, `Preparing`, `Out for Delivery`, `Delivered`, `Cancelled` filters, Accept/Reject, status updates, and Order Details modal), and **Reset Demo Data** synced with Customer App via `localStorage`.
- **PWA Support**:
  - `manifest.json` (`"name": "GramKart"`, `"display": "standalone"`, `#15803D` / `#FFFFFF` branding, `192x192` and `512x512` `any` + `maskable` icons)
  - `service-worker.js` (dynamic base-path & scope caching for offline support in subdirectory hosting)
  - `index.html`, `app.js`, `styles.css` using subdirectory-safe `./` paths
  - `.nojekyll` to bypass Jekyll processing on GitHub Pages

## Automatic Deployment to GitHub Pages (HTTPS)

This repository includes `.github/workflows/deploy.yml`, which deploys `pwa/` automatically whenever you push to `main`:

1. **Push your repository to GitHub** on the `main` branch.
2. Open your GitHub repository → **Settings → Pages**.
3. Under **Build and deployment → Source**, select **GitHub Actions**.
4. Ensure **Enforce HTTPS** is enabled in **Settings → Pages** (GitHub Pages serves `*.github.io` over HTTPS automatically).
5. Push any commit to `main` (or run **Actions → Deploy GramKart PWA to GitHub Pages → Run workflow**).
6. The workflow automatically:
   - Copies `pwa/` into the Pages artifact
   - Injects your repository's exact base path (`/<repo-name>/` for project sites, or `/` for user/organization sites) into `manifest.json` (`id`, `start_url`, `scope`, and `icons[].src`)
   - Validates `manifest.json`, PNG icon dimensions, assets, and JS syntax
   - Publishes your live site at `https://<username>.github.io/<repo-name>/`

## Local Subdirectory Verification
To simulate GitHub Pages subdirectory hosting locally over HTTPS-equivalent `localhost`:
```bash
python3 -m http.server 8080
```
Open `http://localhost:8080/pwa/` in Chrome to test `./` relative paths, `manifest.json`, and Service Worker registration under a subdirectory path.

## Install on Android Phones
1. Open your deployed `https://<username>.github.io/<repo-name>/` URL in **Google Chrome** on Android.
2. Tap **Install App** on the banner inside GramKart (or tap **⋮ menu → Install app / Add to Home screen**).
3. Tap **Install** — GramKart installs with its custom green icon, opens in **standalone** full-screen mode with the splash screen, and caches core assets offline.
