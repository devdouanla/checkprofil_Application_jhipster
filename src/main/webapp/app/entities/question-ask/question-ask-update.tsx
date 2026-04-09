import React, { useEffect } from 'react';
import { Button, Col, Row } from 'react-bootstrap';
import { Translate, ValidatedField, ValidatedForm, translate } from 'react-jhipster';
import { Link, useNavigate, useParams } from 'react-router';

import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getEntities as getQuestions } from 'app/entities/question/question.reducer';
import { getEntities as getSessionTests } from 'app/entities/session-test/session-test.reducer';

import { createEntity, getEntity, reset, updateEntity } from './question-ask.reducer';

export const QuestionAskUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const questions = useAppSelector(state => state.question.entities);
  const sessionTests = useAppSelector(state => state.sessionTest.entities);
  const questionAskEntity = useAppSelector(state => state.questionAsk.entity);
  const loading = useAppSelector(state => state.questionAsk.loading);
  const updating = useAppSelector(state => state.questionAsk.updating);
  const updateSuccess = useAppSelector(state => state.questionAsk.updateSuccess);

  const handleClose = () => {
    navigate('/question-ask');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getQuestions({}));
    dispatch(getSessionTests({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    if (values.id !== undefined && typeof values.id !== 'number') {
      values.id = Number(values.id);
    }
    if (values.ordre !== undefined && typeof values.ordre !== 'number') {
      values.ordre = Number(values.ordre);
    }

    const entity = {
      ...questionAskEntity,
      ...values,
      question: questions.find(it => it.id.toString() === values.question?.toString()),
      session: sessionTests.find(it => it.id.toString() === values.session?.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {}
      : {
          ...questionAskEntity,
          question: questionAskEntity?.question?.id,
          session: questionAskEntity?.session?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="checkprofileApp.questionAsk.home.createOrEditLabel" data-cy="QuestionAskCreateUpdateHeading">
            <Translate contentKey="checkprofileApp.questionAsk.home.createOrEditLabel">Create or edit a QuestionAsk</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew && (
                <ValidatedField
                  name="id"
                  required
                  readOnly
                  id="question-ask-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              )}
              <ValidatedField
                label={translate('checkprofileApp.questionAsk.ordre')}
                id="question-ask-ordre"
                name="ordre"
                data-cy="ordre"
                type="text"
              />
              <ValidatedField
                id="question-ask-question"
                name="question"
                data-cy="question"
                label={translate('checkprofileApp.questionAsk.question')}
                type="select"
              >
                <option value="" key="0" />
                {questions
                  ? questions.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="question-ask-session"
                name="session"
                data-cy="session"
                label={translate('checkprofileApp.questionAsk.session')}
                type="select"
              >
                <option value="" key="0" />
                {sessionTests
                  ? sessionTests.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button as={Link as any} id="cancel-save" data-cy="entityCreateCancelButton" to="/question-ask" replace variant="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button variant="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default QuestionAskUpdate;
