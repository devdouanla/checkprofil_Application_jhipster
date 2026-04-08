import React, { useEffect } from 'react';
import { Button, Col, Row } from 'react-bootstrap';
import { Translate } from 'react-jhipster';
import { Link, useParams } from 'react-router';

import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './resultat.reducer';

export const ResultatDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const resultatEntity = useAppSelector(state => state.resultat.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="resultatDetailsHeading">
          <Translate contentKey="checkprofileApp.resultat.detail.title">Resultat</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{resultatEntity.id}</dd>
          <dt>
            <span id="scoreTotal">
              <Translate contentKey="checkprofileApp.resultat.scoreTotal">Score Total</Translate>
            </span>
          </dt>
          <dd>{resultatEntity.scoreTotal}</dd>
          <dt>
            <span id="scoreMax">
              <Translate contentKey="checkprofileApp.resultat.scoreMax">Score Max</Translate>
            </span>
          </dt>
          <dd>{resultatEntity.scoreMax}</dd>
          <dt>
            <span id="pourcentage">
              <Translate contentKey="checkprofileApp.resultat.pourcentage">Pourcentage</Translate>
            </span>
          </dt>
          <dd>{resultatEntity.pourcentage}</dd>
          <dt>
            <span id="mention">
              <Translate contentKey="checkprofileApp.resultat.mention">Mention</Translate>
            </span>
          </dt>
          <dd>{resultatEntity.mention}</dd>
        </dl>
        <Button as={Link as any} to="/resultat" replace variant="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button as={Link as any} to={`/resultat/${resultatEntity.id}/edit`} replace variant="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default ResultatDetail;
