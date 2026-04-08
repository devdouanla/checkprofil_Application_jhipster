import React from 'react';
import { Translate } from 'react-jhipster';

import MenuItem from 'app/shared/layout/menus/menu-item';
import { buildManagerworkRoute } from 'app/features/managerwork/managerwork.config';

const EntitiesMenu = () => {
  return (
    <>
      {/* prettier-ignore */}
      <MenuItem icon="asterisk" to="/manager">
        <Translate contentKey="global.menu.entities.manager" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/expert">
        <Translate contentKey="global.menu.entities.expert" />
      </MenuItem>
      <MenuItem icon="asterisk" to={buildManagerworkRoute()}>
        Managerwork
      </MenuItem>
      <MenuItem icon="asterisk" to="/rh">
        <Translate contentKey="global.menu.entities.rh" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/employe">
        <Translate contentKey="global.menu.entities.employe" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/poste">
        <Translate contentKey="global.menu.entities.poste" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/competence">
        <Translate contentKey="global.menu.entities.competence" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/competence-requise">
        <Translate contentKey="global.menu.entities.competenceRequise" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/epreuve">
        <Translate contentKey="global.menu.entities.epreuve" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/question">
        <Translate contentKey="global.menu.entities.question" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/evaluation">
        <Translate contentKey="global.menu.entities.evaluation" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/session-test">
        <Translate contentKey="global.menu.entities.sessionTest" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/resultat">
        <Translate contentKey="global.menu.entities.resultat" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/reponse-candidat">
        <Translate contentKey="global.menu.entities.reponseCandidat" />
      </MenuItem>
      {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
    </>
  );
};

export default EntitiesMenu;
