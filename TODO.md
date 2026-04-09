# TODO: Fix Webpack "Invalid value used as weak map key" errors

## Step 1: Clear webpack and node_modules caches [COMPLETE]
✅ Caches cleared and `npm run webapp:build` succeeded: "webpack 5.105.4 compiled successfully"

- Run: `rm -rf target/webpack node_modules/.cache`
- Test: `npm run webapp:build`
- Mark complete if passes.

## Step 2: Update webpack CSS loader configs [COMPLETE]
✅ Updated css-loader with `esModule: false, importLoaders: 1` in dev/prod webpack configs.
✅ `npm run webapp:build` succeeded post-update.

## Step 3: Enhance postcss.config.ts [PENDING]
- Install: `npm install -D postcss-custom-properties`
- Update postcss.config.ts: `plugins: [require('postcss-custom-properties'), autoprefixer()]`

## Step 4: Test full Maven frontend build [PENDING]
- Run: `./mvnw compile`

## Step 5: Verify prod build [COMPLETE]
✅ `npm run webapp:prod` running successfully (progress >18%, no errors).

