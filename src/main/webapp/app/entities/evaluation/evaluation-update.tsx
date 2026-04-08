import React, { useEffect } from 'react';
import { Button, Col, Row } from 'react-bootstrap';
import { Translate, ValidatedField, ValidatedForm, translate } from 'react-jhipster';
import { Link, useNavigate, useParams } from 'react-router';

import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getEntities as getEmployes } from 'app/entities/employe/employe.reducer';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';

import { createEntity, getEntity, reset, updateEntity } from './evaluation.reducer';

export const EvaluationUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const employes = useAppSelector(state => state.employe.entities);
  const evaluationEntity = useAppSelector(state => state.evaluation.entity);
  const loading = useAppSelector(state => state.evaluation.loading);
  const updating = useAppSelector(state => state.evaluation.updating);
  const updateSuccess = useAppSelector(state => state.evaluation.updateSuccess);

  const handleClose = () => {
    navigate(`/evaluation${location.search}`);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getEmployes({}));
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
    values.dateDebut = convertDateTimeToServer(values.dateDebut);
    values.dateFin = convertDateTimeToServer(values.dateFin);

    const entity = {
      ...evaluationEntity,
      ...values,
      employe: employes.find(it => it.id.toString() === values.employe?.toString()),
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
          dateFin: displayDefaultDateTime(),
        }
      : {
          ...evaluationEntity,
          dateDebut: convertDateTimeFromServer(evaluationEntity.dateDebut),
          dateFin: convertDateTimeFromServer(evaluationEntity.dateFin),
          employe: evaluationEntity?.employe?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="checkprofileApp.evaluation.home.createOrEditLabel" data-cy="EvaluationCreateUpdateHeading">
            <Translate contentKey="checkprofileApp.evaluation.home.createOrEditLabel">Create or edit a Evaluation</Translate>
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
                  id="evaluation-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              )}
              <ValidatedField
                label={translate('checkprofileApp.evaluation.dateDebut')}
                id="evaluation-dateDebut"
                name="dateDebut"
                data-cy="dateDebut"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('checkprofileApp.evaluation.dateFin')}
                id="evaluation-dateFin"
                name="dateFin"
                data-cy="dateFin"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('checkprofileApp.evaluation.conforme')}
                id="evaluation-conforme"
                name="conforme"
                data-cy="conforme"
                check
                type="checkbox"
              />
              <ValidatedField
                id="evaluation-employe"
                name="employe"
                data-cy="employe"
                label={translate('checkprofileApp.evaluation.employe')}
                type="select"
              >
                <option value="" key="0" />
                {employes
                  ? employes.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button as={Link as any} id="cancel-save" data-cy="entityCreateCancelButton" to="/evaluation" replace variant="info">
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

export default EvaluationUpdate;
