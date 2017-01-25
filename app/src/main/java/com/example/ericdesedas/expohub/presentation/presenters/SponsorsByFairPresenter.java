package com.example.ericdesedas.expohub.presentation.presenters;

import com.example.ericdesedas.expohub.data.models.ApiErrorWrapper;
import com.example.ericdesedas.expohub.data.models.Sponsor;
import com.example.ericdesedas.expohub.domain.interactors.ApiUseCase;
import com.example.ericdesedas.expohub.domain.interactors.GetSponsorsByFairUseCase;
import com.example.ericdesedas.expohub.presentation.activities.SponsorsByFairActivity;

import java.util.List;

import moe.banana.jsonapi2.Document;

public class SponsorsByFairPresenter extends Presenter {

    private GetSponsorsByFairUseCase useCase;
    private View view;

    private ApiUseCase.Listener<Document<Sponsor>> useCaseListener = new ApiUseCase.Listener<Document<Sponsor>>() {
        @Override
        public void onResponse(int statusCode, Document<Sponsor> result) {

            view.toggleLoading(false);

            List<Sponsor> sponsorList   = generateArrayFromDocument(result);
            Sponsor[] sponsors          = sponsorList.toArray(new Sponsor[sponsorList.size()]);

            view.updateList(sponsors);
        }

        @Override
        public void onError(int statusCode, ApiErrorWrapper apiError) {
            view.toggleLoading(false);
            if (apiError.hasUniqueError()) {
                view.showError(statusCode, apiError.getUniqueError().getDetail());
            } else {
                String errorString = concatenateErrorString(apiError.getErrorList());
                view.showError(statusCode, errorString);
            }
        }

        @Override
        public void onFailure(Throwable throwable) {
            view.toggleLoading(false);
            view.showError(500, "");
        }
    };

    public SponsorsByFairPresenter(GetSponsorsByFairUseCase useCase) {
        this.useCase = useCase;
    }

    @Override
    public void onStart() {
        super.onStart();
        useCase.registerListener(useCaseListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        useCase.unregisterListener(useCaseListener);
    }

    public void setView(View view) {
        this.view = view;
    }

    public void initialize() {

        useCase.addParameter("include", "sponsorRank");
        useCase.addParameter("sort", "sponsor_rank_id");
    }

    // Commands

    public void onLoadSponsorsCommand(String fairId) {
        view.toggleLoading(true);
        useCase.executeRequest(fairId);
    }

    public interface View {
        void toggleLoading(boolean showLoading);
        void updateList(Sponsor[] sponsors);
        void showError(int code, String error);
    }
}
