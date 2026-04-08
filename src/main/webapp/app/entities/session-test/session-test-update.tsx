import React, { useEffect } from 'react';
import { Button, Col, Row } from 'react-bootstrap';
import { Translate, ValidatedField, ValidatedForm, translate } from 'react-jhipster';
import { Link, useNavigate, useParams } from 'react-router';

import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getEntities as getEpreuves } from 'app/entities/epreuve/epreuve.reducer';
import { getEntities as getEvaluations } from 'app/entities/evaluation/evaluation.reducer';
import { getEntities as getResultats } from 'app/entities/resultat/resultat.reducer';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';

import { createEntity, getEntity, reset, updateEntity } from './session-test.reducer';

export const SessionTestUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const resultats = useAppSelector(state => state.resultat.entities);
  const evaluations = useAppSelector(state => state.evaluation.entities);
  const epreuves = useAppSelector(state => state.epreuve.entities);
  const sessionTestEntity = useAppSelector(state => state.sessionTest.entity);
  const loading = useAppSelector(state => state.sessionTest.loading);
  const updating = useAppSelector(state => state.sessionTest.updating);
  const updateSuccess = useAppSelector(state => state.sessionTest.updateSuccess);

  const handleClose = () => {
    navigate(`/session-test${location.search}`);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getResultats({}));
    dispatch(getEvaluations({}));
    dispatch(getEpreuves({}));
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
    if (values.scoreObtenu !== undefined && typeof values.scoreObtenu !== 'number') {
      values.scoreObtenu = Number(values.scoreObtenu);
    }
    values.dateDebut = convertDateTimeToServer(values.dateDebut);

    const entity = {
      ...sessionTestEntity,
      ...values,
      resultat: resultats.find(it => it.id.toString() === values.resultat?.toString()),
      evaluation: evaluations.find(it => it.id.toString() === values.evaluation?.toString()),
      epreuves: epreuves.find(it => it.id.toString() === values.epreuves?.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {
          dateDebut: displayDefaultDateTime(),
        }
      : {
          ...sessionTestEntity,
          dateDebut: convertDateTimeFromServer(sessionTestEntity.dateDebut),
          resultat: sessionTestEntity?.resultat?.id,
          evaluation: sessionTestEntity?.evaluation?.id,
          epreuves: sessionTestEntity?.epreuves?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="checkprofileApp.sessionTest.home.createOrEditLabel" data-cy="SessionTestCreateUpdateHeading">
            <Translate contentKey="checkprofileApp.sessionTest.home.createOrEditLabel">Create or edit a SessionTest</Translate>
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
                  id="session-test-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              )}
              <ValidatedField
                label={translate('checkprofileApp.sessionTest.scoreObtenu')}
                id="session-test-scoreObtenu"
                name="scoreObtenu"
                data-cy="scoreObtenu"
                type="text"
              />
              <ValidatedField
                label={translate('checkprofileApp.sessionTest.dateDebut')}
                id="session-test-dateDebut"
                name="dateDebut"
                data-cy="dateDebut"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                id="session-test-resultat"
                name="resultat"
                data-cy="resultat"
                label={translate('checkprofileApp.sessionTest.resultat')}
                type="select"
              >
                <option value="" key="0" />
                {resultats
                  ? resultats.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="session-test-evaluation"
                name="evaluation"
                data-cy="evaluation"
                label={translate('checkprofileApp.sessionTest.evaluation')}
                type="select"
              >
                <option value="" key="0" />
                {evaluations
                  ? evaluations.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="session-test-epreuves"
                name="epreuves"
                data-cy="epreuves"
                label={translate('checkprofileApp.sessionTest.epreuves')}
                type="select"
              >
                <option value="" key="0" />
                {epreuves
                  ? epreuves.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button as={Link as any} id="cancel-save" data-cy="entityCreateCancelButton" to="/session-test" replace variant="info">
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

export default SessionTestUpdate;
