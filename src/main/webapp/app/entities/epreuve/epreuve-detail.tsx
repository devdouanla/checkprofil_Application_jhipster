import React, { useEffect } from 'react';
import { Button, Col, Row } from 'react-bootstrap';
import { Translate } from 'react-jhipster';
import { Link, useParams } from 'react-router';

import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './epreuve.reducer';

export const EpreuveDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const epreuveEntity = useAppSelector(state => state.epreuve.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="epreuveDetailsHeading">
          <Translate contentKey="checkprofileApp.epreuve.detail.title">Epreuve</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{epreuveEntity.id}</dd>
          <dt>
            <span id="titre">
              <Translate contentKey="checkprofileApp.epreuve.titre">Titre</Translate>
            </span>
          </dt>
          <dd>{epreuveEntity.titre}</dd>
          <dt>
            <span id="enonce">
              <Translate contentKey="checkprofileApp.epreuve.enonce">Enonce</Translate>
            </span>
          </dt>
          <dd>{epreuveEntity.enonce}</dd>
          <dt>
            <span id="difficulte">
              <Translate contentKey="checkprofileApp.epreuve.difficulte">Difficulte</Translate>
            </span>
          </dt>
          <dd>{epreuveEntity.difficulte}</dd>
          <dt>
            <span id="duree">
              <Translate contentKey="checkprofileApp.epreuve.duree">Duree</Translate>
            </span>
          </dt>
          <dd>{epreuveEntity.duree}</dd>
          <dt>
            <span id="nbQuestions">
              <Translate contentKey="checkprofileApp.epreuve.nbQuestions">Nb Questions</Translate>
            </span>
          </dt>
          <dd>{epreuveEntity.nbQuestions}</dd>
          <dt>
            <span id="genereParIA">
              <Translate contentKey="checkprofileApp.epreuve.genereParIA">Genere Par IA</Translate>
            </span>
          </dt>
          <dd>{epreuveEntity.genereParIA ? 'true' : 'false'}</dd>
          <dt>
            <span id="publie">
              <Translate contentKey="checkprofileApp.epreuve.publie">Publie</Translate>
            </span>
          </dt>
          <dd>{epreuveEntity.publie ? 'true' : 'false'}</dd>
          <dt>
            <Translate contentKey="checkprofileApp.epreuve.competence">Competence</Translate>
          </dt>
          <dd>{epreuveEntity.competence ? epreuveEntity.competence.id : ''}</dd>
        </dl>
        <Button as={Link as any} to="/epreuve" replace variant="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button as={Link as any} to={`/epreuve/${epreuveEntity.id}/edit`} replace variant="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default EpreuveDetail;
