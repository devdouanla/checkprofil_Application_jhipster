import React, { useEffect, useState } from 'react';
import { Button, Table } from 'react-bootstrap';
import { TextFormat, Translate, getSortState } from 'react-jhipster';
import { Link, useLocation, useNavigate } from 'react-router';

import { faSort, faSortDown, faSortUp } from '@fortawesome/free-solid-svg-icons';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { overrideSortStateWithQueryParams } from 'app/shared/util/entity-utils';
import { ASC, DESC } from 'app/shared/util/pagination.constants';

import { getEntities } from './reponse-candidat.reducer';

export const ReponseCandidat = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [sortState, setSortState] = useState(overrideSortStateWithQueryParams(getSortState(pageLocation, 'id'), pageLocation.search));

  const reponseCandidatList = useAppSelector(state => state.reponseCandidat.entities);
  const loading = useAppSelector(state => state.reponseCandidat.loading);

  const getAllEntities = () => {
    dispatch(
      getEntities({
        sort: `${sortState.sort},${sortState.order}`,
      }),
    );
  };

  const sortEntities = () => {
    getAllEntities();
    const endURL = `?sort=${sortState.sort},${sortState.order}`;
    if (pageLocation.search !== endURL) {
      navigate(`${pageLocation.pathname}${endURL}`);
    }
  };

  useEffect(() => {
    sortEntities();
  }, [sortState.order, sortState.sort]);

  const sort = p => () => {
    setSortState({
      ...sortState,
      order: sortState.order === ASC ? DESC : ASC,
      sort: p,
    });
  };

  const handleSyncList = () => {
    sortEntities();
  };

  const getSortIconByFieldName = (fieldName: string) => {
    const sortFieldName = sortState.sort;
    const order = sortState.order;
    if (sortFieldName !== fieldName) {
      return faSort;
    }
    return order === ASC ? faSortUp : faSortDown;
  };

  return (
    <div>
      <h2 id="reponse-candidat-heading" data-cy="ReponseCandidatHeading">
        <Translate contentKey="checkprofileApp.reponseCandidat.home.title">Reponse Candidats</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" variant="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="checkprofileApp.reponseCandidat.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/reponse-candidat/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="checkprofileApp.reponseCandidat.home.createLabel">Create new Reponse Candidat</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {reponseCandidatList?.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="checkprofileApp.reponseCandidat.id">ID</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('estCorrecte')}>
                  <Translate contentKey="checkprofileApp.reponseCandidat.estCorrecte">Est Correcte</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('estCorrecte')} />
                </th>
                <th className="hand" onClick={sort('dateReponse')}>
                  <Translate contentKey="checkprofileApp.reponseCandidat.dateReponse">Date Reponse</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('dateReponse')} />
                </th>
                <th>
                  <Translate contentKey="checkprofileApp.reponseCandidat.question">Question</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="checkprofileApp.reponseCandidat.session">Session</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {reponseCandidatList.map(reponseCandidat => (
                <tr key={`entity-${reponseCandidat.id}`} data-cy="entityTable">
                  <td>
                    <Button as={Link as any} to={`/reponse-candidat/${reponseCandidat.id}`} variant="link" size="sm">
                      {reponseCandidat.id}
                    </Button>
                  </td>
                  <td>{reponseCandidat.estCorrecte ? 'true' : 'false'}</td>
                  <td>
                    {reponseCandidat.dateReponse ? (
                      <TextFormat type="date" value={reponseCandidat.dateReponse} format={APP_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>
                    {reponseCandidat.question ? (
                      <Link to={`/question/${reponseCandidat.question.id}`}>{reponseCandidat.question.id}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td>
                    {reponseCandidat.session ? (
                      <Link to={`/session-test/${reponseCandidat.session.id}`}>{reponseCandidat.session.id}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button
                        as={Link as any}
                        to={`/reponse-candidat/${reponseCandidat.id}`}
                        variant="info"
                        size="sm"
                        data-cy="entityDetailsButton"
                      >
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button
                        as={Link as any}
                        to={`/reponse-candidat/${reponseCandidat.id}/edit`}
                        variant="primary"
                        size="sm"
                        data-cy="entityEditButton"
                      >
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button
                        onClick={() => (window.location.href = `/reponse-candidat/${reponseCandidat.id}/delete`)}
                        variant="danger"
                        size="sm"
                        data-cy="entityDeleteButton"
                      >
                        <FontAwesomeIcon icon="trash" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.delete">Delete</Translate>
                        </span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && (
            <div className="alert alert-warning">
              <Translate contentKey="checkprofileApp.reponseCandidat.home.notFound">No Reponse Candidats found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default ReponseCandidat;
