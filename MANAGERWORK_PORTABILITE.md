# Portabilite de la feature Managerwork

Ce guide explique comment integrer `managerwork` dans une autre application **sans modifier la logique metier Java**.

## Principe d integration

La feature est decouplee via une configuration runtime front:

- Route de base configurable (par defaut: `/managerwork`)
- Prefixe API configurable (par defaut: `/api`)
- Noms des endpoints configurables (ex: `evaluations`, `session-tests`, etc.)

Configuration supportee dans `window.__MANAGERWORK_CONFIG__`.

## Etapes pour transporter vers un autre projet

1. Copier le dossier `src/main/webapp/app/features/managerwork`.
2. Enregistrer le reducer dans le store:
   - importer `managerwork` depuis `app/features/managerwork/managerwork.reducer`
   - l ajouter dans l objet des reducers sous la cle `managerwork`.
3. Brancher les routes:
   - importer `ManagerworkModuleRoutes`
   - importer `getManagerworkRoutePath`
   - ajouter une route `path={getManagerworkRoutePath()}` vers `<ManagerworkModuleRoutes />`.
4. Ajouter un acces UI (menu/home) avec `buildManagerworkRoute(...)`.
5. Declarer la config runtime (si necessaire) dans le host:

```html
<script>
  window.__MANAGERWORK_CONFIG__ = {
    routeBasePath: '/espace-manager',
    apiPrefix: '/api',
    endpoints: {
      managers: 'managers',
      evaluations: 'evaluations',
      employes: 'employes',
      postes: 'postes',
      competenceRequises: 'competence-requises',
      epreuves: 'epreuves',
      questions: 'questions',
      sessionTests: 'session-tests'
    }
  };
</script>
```

6. Verifier les autorites/roles deja existants dans le projet d accueil (`MANAGER`).
7. Lancer les tests front et verifier la navigation:
   - dashboard
   - wizard
   - conduite d evaluation.

## Compatibilite backend

La logique metier Java n est pas modifiee.
La feature suppose seulement la disponibilite des endpoints REST deja exposes par le backend.
