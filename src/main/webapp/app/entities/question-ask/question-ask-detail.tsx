import React, { useEffect } from 'react';
import { Button, Col, Row } from 'react-bootstrap';
import { Translate } from 'react-jhipster';
import { Link, useParams } from 'react-router';

import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './question-ask.reducer';

export const QuestionAskDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const questionAskEntity = useAppSelector(state => state.questionAsk.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="questionAskDetailsHeading">
          <Translate contentKey="checkprofileApp.questionAsk.detail.title">QuestionAsk</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{questionAskEntity.id}</dd>
          <dt>
            <span id="ordre">
              <Translate contentKey="checkprofileApp.questionAsk.ordre">Ordre</Translate>
            </span>
          </dt>
          <dd>{questionAskEntity.ordre}</dd>
          <dt>
            <Translate contentKey="checkprofileApp.questionAsk.question">Question</Translate>
          </dt>
          <dd>{questionAskEntity.question ? questionAskEntity.question.id : ''}</dd>
          <dt>
            <Translate contentKey="checkprofileApp.questionAsk.session">Session</Translate>
          </dt>
          <dd>{questionAskEntity.session ? questionAskEntity.session.id : ''}</dd>
        </dl>
        <Button as={Link as any} to="/question-ask" replace variant="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button as={Link as any} to={`/question-ask/${questionAskEntity.id}/edit`} replace variant="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default QuestionAskDetail;
