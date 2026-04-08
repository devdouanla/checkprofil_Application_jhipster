import React, { useEffect } from 'react';
import { Button, Col, Row } from 'react-bootstrap';
import { Translate, ValidatedField, ValidatedForm, isNumber, translate } from 'react-jhipster';
import { Link, useNavigate, useParams } from 'react-router';

import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';
import { Mention } from 'app/shared/model/enumerations/mention.model';

import { createEntity, getEntity, reset, updateEntity } from './resultat.reducer';

export const ResultatUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const resultatEntity = useAppSelector(state => state.resultat.entity);
  const loading = useAppSelector(state => state.resultat.loading);
  const updating = useAppSelector(state => state.resultat.updating);
  const updateSuccess = useAppSelector(state => state.resultat.updateSuccess);
  const mentionValues = Object.keys(Mention);

  const handleClose = () => {
    navigate(`/resultat${location.search}`);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }
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
    if (values.scoreTotal !== undefined && typeof values.scoreTotal !== 'number') {
      values.scoreTotal = Number(values.scoreTotal);
    }
    if (values.scoreMax !== undefined && typeof values.scoreMax !== 'number') {
      values.scoreMax = Number(values.scoreMax);
    }
    if (values.pourcentage !== undefined && typeof values.pourcentage !== 'number') {
      values.pourcentage = Number(values.pourcentage);
    }

    const entity = {
      ...resultatEntity,
      ...values,
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
          mention: 'EXCELLENT',
          ...resultatEntity,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="checkprofileApp.resultat.home.createOrEditLabel" data-cy="ResultatCreateUpdateHeading">
            <Translate contentKey="checkprofileApp.resultat.home.createOrEditLabel">Create or edit a Resultat</Translate>
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
                  id="resultat-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              )}
              <ValidatedField
                label={translate('checkprofileApp.resultat.scoreTotal')}
                id="resultat-scoreTotal"
                name="scoreTotal"
                data-cy="scoreTotal"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('checkprofileApp.resultat.scoreMax')}
                id="resultat-scoreMax"
                name="scoreMax"
                data-cy="scoreMax"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('checkprofileApp.resultat.pourcentage')}
                id="resultat-pourcentage"
                name="pourcentage"
                data-cy="pourcentage"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('checkprofileApp.resultat.mention')}
                id="resultat-mention"
                name="mention"
                data-cy="mention"
                type="select"
              >
                {mentionValues.map(mention => (
                  <option value={mention} key={mention}>
                    {translate(`checkprofileApp.Mention.${mention}`)}
                  </option>
                ))}
              </ValidatedField>
              <Button as={Link as any} id="cancel-save" data-cy="entityCreateCancelButton" to="/resultat" replace variant="info">
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

export default ResultatUpdate;
