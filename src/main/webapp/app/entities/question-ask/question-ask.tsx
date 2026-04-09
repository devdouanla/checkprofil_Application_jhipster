import React, { useEffect, useState } from 'react';
import { Button, Table } from 'react-bootstrap';
import { Translate, getSortState } from 'react-jhipster';
import { Link, useLocation, useNavigate } from 'react-router';

import { faSort, faSortDown, faSortUp } from '@fortawesome/free-solid-svg-icons';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';
import { overrideSortStateWithQueryParams } from 'app/shared/util/entity-utils';
import { ASC, DESC } from 'app/shared/util/pagination.constants';

import { getEntities } from './question-ask.reducer';

export const QuestionAsk = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [sortState, setSortState] = useState(overrideSortStateWithQueryParams(getSortState(pageLocation, 'id'), pageLocation.search));

  const questionAskList = useAppSelector(state => state.questionAsk.entities);
  const loading = useAppSelector(state => state.questionAsk.loading);

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
      <h2 id="question-ask-heading" data-cy="QuestionAskHeading">
        <Translate contentKey="checkprofileApp.questionAsk.home.title">Question Asks</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" variant="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="checkprofileApp.questionAsk.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/question-ask/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="checkprofileApp.questionAsk.home.createLabel">Create new Question Ask</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {questionAskList?.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="checkprofileApp.questionAsk.id">ID</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('ordre')}>
                  <Translate contentKey="checkprofileApp.questionAsk.ordre">Ordre</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('ordre')} />
                </th>
                <th>
                  <Translate contentKey="checkprofileApp.questionAsk.question">Question</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="checkprofileApp.questionAsk.session">Session</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {questionAskList.map(questionAsk => (
                <tr key={`entity-${questionAsk.id}`} data-cy="entityTable">
                  <td>
                    <Button as={Link as any} to={`/question-ask/${questionAsk.id}`} variant="link" size="sm">
                      {questionAsk.id}
                    </Button>
                  </td>
                  <td>{questionAsk.ordre}</td>
                  <td>{questionAsk.question ? <Link to={`/question/${questionAsk.question.id}`}>{questionAsk.question.id}</Link> : ''}</td>
                  <td>{questionAsk.session ? <Link to={`/session-test/${questionAsk.session.id}`}>{questionAsk.session.id}</Link> : ''}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button
                        as={Link as any}
                        to={`/question-ask/${questionAsk.id}`}
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
                        to={`/question-ask/${questionAsk.id}/edit`}
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
                        onClick={() => (window.location.href = `/question-ask/${questionAsk.id}/delete`)}
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
              <Translate contentKey="checkprofileApp.questionAsk.home.notFound">No Question Asks found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default QuestionAsk;
