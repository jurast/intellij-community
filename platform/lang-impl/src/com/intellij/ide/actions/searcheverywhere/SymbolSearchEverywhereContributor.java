// Copyright 2000-2018 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
package com.intellij.ide.actions.searcheverywhere;

import com.intellij.ide.IdeBundle;
import com.intellij.ide.util.gotoByName.FilteringGotoByModel;
import com.intellij.ide.util.gotoByName.GotoSymbolModel2;
import com.intellij.ide.util.gotoByName.LanguageRef;
import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.NlsContexts;
import com.intellij.ui.IdeUICustomization;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * @author Konstantin Bulenkov
 */
public class SymbolSearchEverywhereContributor extends AbstractGotoSEContributor {

  private final PersistentSearchEverywhereContributorFilter<LanguageRef> myFilter;

  public SymbolSearchEverywhereContributor(@NotNull AnActionEvent event) {
    super(event);
    myFilter = ClassSearchEverywhereContributor.createLanguageFilter(event.getRequiredData(CommonDataKeys.PROJECT));
  }

  @NotNull
  @Override
  public String getGroupName() {
    return IdeBundle.message("search.everywhere.group.name.symbols");
  }

  @NotNull
  @NlsContexts.Checkbox
  public String includeNonProjectItemsText() {
    return IdeUICustomization.getInstance().projectMessage("checkbox.include.non.project.symbols");
  }

  @Override
  public int getSortWeight() {
    return 300;
  }

  @NotNull
  @Override
  protected FilteringGotoByModel<LanguageRef> createModel(@NotNull Project project) {
    GotoSymbolModel2 model = new GotoSymbolModel2(project);
    if (myFilter != null) {
      model.setFilterItems(myFilter.getSelectedElements());
    }
    return model;
  }

  @Override
  protected @Nullable SearchEverywhereCommandInfo getFilterCommand() {
    return new SearchEverywhereCommandInfo("s", IdeBundle.message("search.everywhere.filter.symbols.description"), this);
  }

  @NotNull
  @Override
  public List<AnAction> getActions(@NotNull Runnable onChanged) {
    return doGetActions(includeNonProjectItemsText(), myFilter, onChanged);
  }

  public static class Factory implements SearchEverywhereContributorFactory<Object> {
    @NotNull
    @Override
    public SearchEverywhereContributor<Object> createContributor(@NotNull AnActionEvent initEvent) {
      return new SymbolSearchEverywhereContributor(initEvent);
    }
  }
}
