import React, { useEffect } from 'react';
import { Button, Col, Row } from 'react-bootstrap';
import { Translate } from 'react-jhipster';
import { Link, useParams } from 'react-router';

import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './competence-requise.reducer';

export const CompetenceRequiseDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const competenceRequiseEntity = useAppSelector(state => state.competenceRequise.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="competenceRequiseDetailsHeading">
          <Translate contentKey="checkprofileApp.competenceRequise.detail.title">CompetenceRequise</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{competenceRequiseEntity.id}</dd>
          <dt>
            <span id="obligatoire">
              <Translate contentKey="checkprofileApp.competenceRequise.obligatoire">Obligatoire</Translate>
            </span>
          </dt>
          <dd>{competenceRequiseEntity.obligatoire ? 'true' : 'false'}</dd>
          <dt>
            <Translate contentKey="checkprofileApp.competenceRequise.competence">Competence</Translate>
          </dt>
          <dd>{competenceRequiseEntity.competence ? competenceRequiseEntity.competence.id : ''}</dd>
          <dt>
            <Translate contentKey="checkprofileApp.competenceRequise.poste">Poste</Translate>
          </dt>
          <dd>{competenceRequiseEntity.poste ? competenceRequiseEntity.poste.id : ''}</dd>
        </dl>
        <Button as={Link as any} to="/competence-requise" replace variant="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button as={Link as any} to={`/competence-requise/${competenceRequiseEntity.id}/edit`} replace variant="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default CompetenceRequiseDetail;
