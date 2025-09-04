package knemognition.heartauth.orchestrator.shared.app.ports.out;


import knemognition.heartauth.orchestrator.shared.app.domain.CreatedFlowResult;


public interface CreateFlowStore<T> {
    CreatedFlowResult create(T state);
}

