package knemognition.heartauth.orchestrator.internal.app.ports.out;


import knemognition.heartauth.orchestrator.internal.app.domain.CreatedFlowResult;


public interface CreateFlowStore<T> {
    CreatedFlowResult create(T state);
}

