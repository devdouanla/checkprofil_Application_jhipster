import React, { useEffect } from 'react';
import { Button, Col, Row } from 'react-bootstrap';
import { Translate } from 'react-jhipster';
import { Link, useParams } from 'react-router';

import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './competence.reducer';

export const CompetenceDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const competenceEntity = useAppSelector(state => state.competence.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="competenceDetailsHeading">
          <Translate contentKey="checkprofileApp.competence.detail.title">Competence</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{competenceEntity.id}</dd>
          <dt>
            <span id="nom">
              <Translate contentKey="checkprofileApp.competence.nom">Nom</Translate>
            </span>
          </dt>
          <dd>{competenceEntity.nom}</dd>
          <dt>
            <Translate contentKey="checkprofileApp.competence.experts">Experts</Translate>
          </dt>
          <dd>
            {competenceEntity.expertses
              ? competenceEntity.expertses.map((val, i) => (
                  <span key={val.id}>
                    <a>{val.id}</a>
                    {competenceEntity.expertses && i === competenceEntity.expertses.length - 1 ? '' : ', '}
                  </span>
                ))
              : null}
          </dd>
        </dl>
        <Button as={Link as any} to="/competence" replace variant="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button as={Link as any} to={`/competence/${competenceEntity.id}/edit`} replace variant="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default CompetenceDetail;
